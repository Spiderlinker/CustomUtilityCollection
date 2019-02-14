package de.spiderlinker.network.server;

import de.spiderlinker.network.data.DataPackage;
import de.spiderlinker.network.data.Executable;
import de.spiderlinker.network.utils.ConnectionUtils;
import de.spiderlinker.network.utils.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);

  private final Map<String, Executable> registeredMethods = new HashMap<>();

  private SSLServerSocket serverSocket;
  private int serverListeningPort;
  private int clientLimit;
  private Thread serverHandleThread;
  private boolean isServerAlive;

  public ServerManager(final int port) {
    this(port, SocketUtils.DEFAULT_LIMIT);
  }

  public ServerManager(final int port, final int clientLimit) {
    this.setPort(port);
    this.setClientLimit(clientLimit);
    this.initialize();
  }

  private void setPort(final int port) {
    this.serverListeningPort = SocketUtils.checkPort(port);
  }

  private void setClientLimit(int limit) {
    this.clientLimit = limit;
  }

  /**
   * This method is called when the server is getting initialized <br>
   * Put code in here to run it before the server starts (Nothing in here yet)
   */
  protected void initialize() {
  }

  /**
   * Register the executable to the id. Every incoming data with
   * the specified id will be handed over to the passed executable.
   *
   * @param id   id of data to claim
   * @param exec executable to be executed when data with specified id will arrive
   */
  public void registerMethod(final String id, final Executable exec) {
    this.registeredMethods.put(id, exec);
  }

  /**
   * This method will be called if there was no suitable method was registered for the received data.
   * This method is empty by default.
   *
   * @param data   DataPackage that could not be assigned to a registered method
   * @param socket client that sent this data
   */
  protected void onUnidentifiedMessage(final DataPackage data, final Socket socket) {
    LOGGER.warn("No implementation for handling of unidentified messages!\nUnidentified data '{}' from '{}'", data, socket);
  }

  /**
   * Send given data to specified client. The specified client and data must not be null!
   * The client must be able to perform a handshake and reading the data!
   *
   * @param host address of client
   * @param port port of client
   * @param msg  message to send
   * @throws IOException failed to send message
   */
  public void sendMessage(final String host, final int port, final DataPackage msg)
      throws IOException {
    this.sendMessage(SocketUtils.createSSLSocket(host, port), msg);
  }

  /**
   * Send given data to specified client. The specified client and data must not be null!
   * The client must be able to perform a handshake and reading the data!
   *
   * @param socket address of client / recipient
   * @param msg    message to send
   * @throws IOException failed to send message
   */
  public void sendMessage(final SSLSocket socket, final DataPackage msg) throws IOException {
    if (socket == null || msg == null) {
      // do not send data
      return;
    }

    try {
      ConnectionUtils.performHandshake(socket);
      ConnectionUtils.writeObject(socket, msg);
    } catch (final IOException e) {
      LOGGER.error("Error while sending message to client! The client probably closed the connection without saying Good Bye.");
      throw e;
    }
  }

  private void createServerListeningThread() {
    this.serverHandleThread = new Thread(() -> {
      try {
        createServerSocket();
        waitForIncomingConnections();
      } catch (final IOException e) {
        LOGGER.error("Error while starting server", e);
      }
    });
  }

  private void createServerSocket() throws IOException {
    LOGGER.info("Creating new server listening socket on {} with limit {}", serverListeningPort, clientLimit);
    this.serverSocket = SocketUtils.createSSLServerSocket(this.serverListeningPort, this.clientLimit);
  }

  private void waitForIncomingConnections() {
    LOGGER.info("Start listening... >> {}", serverSocket);
    while (ServerManager.this.isServerAlive) {
      LOGGER.info("Waiting for clientSocket...");
      handleIncomingClientConnectionInNewThread();
    }
  }

  private void handleIncomingClientConnectionInNewThread() {
    try {
      SSLSocket client = (SSLSocket) this.serverSocket.accept();
      Runnable clientHandle = createConnectionHandle(client);
      new Thread(clientHandle).start();
    } catch (final IOException e) {
      // If the server was stopped the #accept() method will throw this error
      // because the socket was closed. It is no error (only if server is alive)
      if (isServerAlive) {
        LOGGER.error("Error while accepting clientSocket", e);
      }
    }
  }

  private Runnable createConnectionHandle(final SSLSocket socket) {
    return () -> {
      try {
        if (!ConnectionUtils.handleHandshake(socket)) {
          return;
        }

        LOGGER.info("Handle connection: {}", socket);
        final Object raw = ConnectionUtils.readObject(socket);

        LOGGER.info("Incoming data (raw): {}", raw);
        processReceivedRawData(raw, socket);

      } catch (IOException | ClassNotFoundException e) {
        LOGGER.error("Error while handling clientSocket connection", e);
      }
    };
  }

  private void processReceivedRawData(Object rawData, SSLSocket client) {
    if (!isDataPackage(rawData)) {
      return;
    }

    final DataPackage data = (DataPackage) rawData;
    Executable dataHandle = getExecutableForReceivedData(data);

    LOGGER.info("Method for incoming data with id '{}' found: {} (from address: {})",
        data.getID(), dataHandle != null /* TODO remove this unnecessary output*/, client);

    runExecutableOrDefault(dataHandle, data, client);
  }

  private boolean isDataPackage(Object rawData) {
    return rawData instanceof DataPackage;
  }

  private Executable getExecutableForReceivedData(DataPackage data) {
    return registeredMethods.get(data.getID());
  }

  private void runExecutableOrDefault(Executable executable, DataPackage data, SSLSocket client) {
    if (executable == null) {
      this.onUnidentifiedMessage(data, client);
    } else {
      executable.run(data, client);
    }
  }

  /**
   * Starts the server if it is not already started
   *
   * @return if the server was successfully started
   */
  public boolean start() {
    boolean started = false;

    LOGGER.info("Starting server (existing socket: {}) at port {}", serverSocket, serverListeningPort);

    if (this.serverSocket == null && !this.isServerAlive) {
      this.createServerListeningThread();
      this.isServerAlive = true;
      this.serverHandleThread.start();
      started = true;
    }
    /* otherwise the server is already started */

    return started;
  }

  /**
   * Stop the server (stop listening thread and close server socket)
   */
  public void stop() {
    LOGGER.info("Stopping server {}", serverSocket);

    if (this.serverHandleThread != null) {
      this.serverHandleThread.interrupt();
    }

    this.isServerAlive = false;
    ConnectionUtils.close(this.serverSocket);
    this.serverSocket = null;
  }

}
