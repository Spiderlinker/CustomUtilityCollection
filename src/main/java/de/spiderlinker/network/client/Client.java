package de.spiderlinker.network.client;

import de.spiderlinker.network.data.DataPackage;
import de.spiderlinker.network.utils.ConnectionUtils;
import de.spiderlinker.network.utils.SocketUtils;
import de.spiderlinker.utils.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

  private String host;
  private int port;
  private int timeout;
  private Socket activeConnection;

  public Client() {
    // create empty clientSocket with no information about host or port
  }

  /**
   * @param host Host to connect to
   * @param port Port of host to connect to
   */
  public Client(final String host, final int port) {
    this(host, port, SocketUtils.DEFAULT_TIMEOUT);
  }

  /**
   * @param host    Host to connect to
   * @param port    Port of host to connect to
   * @param timeout Time to wait for an answer
   */
  public Client(final String host, final int port, int timeout) {
    this.updateConnection(host, port, timeout);
  }

  /**
   * Updates host and port to the passed values
   *
   * @param host    new host
   * @param port    new port
   * @param timeout Time to wait for an answer
   */
  public void updateConnection(final String host, final int port, int timeout) {
    this.updateHost(host);
    this.updatePort(port);
    this.updateTimeout(timeout);
  }

  /**
   * Updates the host to the passed value
   *
   * @param host new host to be set
   */
  public void updateHost(final String host) {
    this.host = StringUtils.requireNonNullOrEmpty(host);
  }

  /**
   * Updates the port to the passed value
   *
   * @param port new port to be set
   */
  public void updatePort(final int port) {
    this.port = SocketUtils.validatePort(port);
  }

  /**
   * Updates the timeout to the host
   *
   * @param timeout timeout to be set
   */
  public void updateTimeout(int timeout) {
    this.timeout = SocketUtils.validateTimeout(timeout);
  }

  /**
   * @return Connected host of this clientSocket
   */
  public String getHost() {
    return host;
  }

  /**
   * @return port of connected host
   */
  public int getPort() {
    return port;
  }

  /**
   * @return timeout of connected host
   */
  public int getTimeout() {
    return timeout;
  }

  /**
   * Sends all passed data with the given id to the specified host (and port)
   *
   * @param id   id of data package
   * @param data data to be sent
   * @throws IOException host not reachable or other connect exception
   */
  public void sendMessage(final String id, final Object... data)
      throws IOException {
    this.sendMessage(new DataPackage(id, data));
  }

  /**
   * Sends all passed data with the given id to the specified host (and port)
   *
   * @param data data to be sent
   * @throws IOException host not reachable or other connect exception
   */
  public void sendMessage(final DataPackage data) throws IOException {
    establishConnection();
    ConnectionUtils.performHandshake(activeConnection);
    ConnectionUtils.writeObject(activeConnection, data);
  }

  /**
   * Receives data from established connection (with given host and port)
   *
   * @return received data from host
   * @throws IOException host not reachable or other network error
   */
  public DataPackage receiveMessage() throws IOException {
    establishConnection();
    ConnectionUtils.handleHandshake(activeConnection);
    return receiveData();
  }

  private DataPackage receiveData() throws IOException {
    DataPackage data = null;
    try {
      Object rawData = ConnectionUtils.readObject(activeConnection);
      if (rawData instanceof DataPackage) {
        data = (DataPackage) rawData;
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return data;
  }

  private void establishConnection() throws IOException {
    if (isConnectionClosed()) {
      activeConnection = SocketUtils.createSocket(host, port, timeout);
    }
  }

  private boolean isConnectionClosed() {
    return activeConnection == null || activeConnection.isClosed();
  }

  /**
   * Closes the active connection (if there is any)
   */
  public void closeConnection() {
    ConnectionUtils.close(activeConnection);
  }

  /**
   * Checks if this clientSocket can connect to the specified host
   *
   * @return whether host is reachable or not
   */
  public boolean isReachable() {
    try {
      return InetAddress.getByName(host).isReachable(SocketUtils.DEFAULT_TIMEOUT);
    } catch (IOException e) {
      // TODO log
      return false;
    }
  }


}
