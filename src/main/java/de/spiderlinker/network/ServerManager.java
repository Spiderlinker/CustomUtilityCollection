package de.spiderlinker.network;

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
  private Thread listeningThread;

  private boolean serverIsAlive;

  public ServerManager(final int port) {
    this.setPort(port);
    this.initialize();
  }

  private void setPort(final int port) {
    this.serverListeningPort = SocketUtils.checkPort(port);
  }

  /**
   * This method is called when the server is getting initialized Put code in
   * here to run it before server starts (Nothing in here yet)
   */
  protected void initialize() {
  }

  public void registerMethod(final String id, final Executable exec) {
    this.registeredMethods.put(id, exec);
  }

  protected void onUnidentifiedMessage(final DataPackage data, final Socket socket) {
    LOGGER.warn("No implementation for handling of unidentified messages!");
  }

  public void sendMessage(final String host, final int port, final DataPackage msg)
      throws IOException {
    this.sendMessage(SocketUtils.createSocket(host, port), msg);
  }

  public void sendMessage(final Socket socket, final DataPackage msg) throws IOException {
    if (socket != null && msg != null) {
      try {
        ConnectionUtils.performHandshake(socket);
        ConnectionUtils.writeObject(socket, msg);
      } catch (final IOException e) {
        /* do stuff -> remove client from clients list */

        throw e;
      }
    }
  }

  private void createServerListeningThread() {
    this.listeningThread = new Thread(() -> {
      try {
        createServerSocket();
        waitForIncomingConnections();
      } catch (final IOException e) {
        LOGGER.error("Error while starting server", e);
      }
    });
  }

  private void createServerSocket() throws IOException {
    this.serverSocket = SocketUtils.createSSLServerSocket(this.serverListeningPort);
  }

  private void waitForIncomingConnections() {
    while (ServerManager.this.serverIsAlive) {
      LOGGER.info("Waiting for client...");
      handleIncomingClientConnectionInNewThread();
    }
  }

  private void handleIncomingClientConnectionInNewThread() {
    try {
      SSLSocket client = (SSLSocket) this.serverSocket.accept();
      Runnable clientHandle = handleConnection(client);
      new Thread(clientHandle).start();
    } catch (final IOException e) {
      LOGGER.error("Error while accepting client", e);
    }
  }

  private Runnable handleConnection(final SSLSocket socket) {
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
        LOGGER.error("Error while handling client connection", e);
      }
    };
  }

  private void processReceivedRawData(Object rawData, SSLSocket client) {
    if (!DataPackage.class.isInstance(rawData)) {
      return;
    }

    final DataPackage data = (DataPackage) rawData;
    Executable dataHandle = getExecutableForReceivedData(data);

    LOGGER.info("Method for incoming data with id '{}' found: {} (from address: {})",
        data.getID(), dataHandle != null /* TODO remove this unnecessary output*/, client);
    runExecutableOrDefaultInThread(dataHandle, data, client);
  }

  private Executable getExecutableForReceivedData(DataPackage data) {
    return registeredMethods.get(data.getID());
  }

  private void runExecutableOrDefaultInThread(Executable executable, DataPackage data, Socket client) {
    new Thread(executable == null //
        ? () -> this.onUnidentifiedMessage(data, client) //
        : () -> executable.run(data, client) //
    ).start();
  }

  public boolean start() {
    boolean started = false;

    if (this.serverSocket == null && !this.serverIsAlive) {
      this.createServerListeningThread();
      this.serverIsAlive = true;
      this.listeningThread.start();
      started = true;
    }
    /* otherwise the server is already started */

    return started;
  }

  /**
   * Stop the server => stop listening thread and close server socket
   */
  public void stop() {
    if (this.listeningThread != null) {
      this.listeningThread.interrupt();
    }

    this.serverIsAlive = false;
    ConnectionUtils.close(this.serverSocket);
    this.serverSocket = null;
  }

}
