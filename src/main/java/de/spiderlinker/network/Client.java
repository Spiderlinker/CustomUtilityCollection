package de.spiderlinker.network;

import de.spiderlinker.network.utils.ConnectionUtils;
import de.spiderlinker.network.utils.SocketUtils;
import de.spiderlinker.utils.StringUtils;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;

public class Client {

  private String host;
  private int port;

  public Client() {
    // create empty client with no information about host or port
  }

  public Client(final String host, final int port) {
    this.updateConnection(host, port);
  }

  /**
   * Updates host and port to the passed values
   *
   * @param host new host
   * @param port new port
   */
  public void updateConnection(final String host, final int port) {
    this.updateHost(host);
    this.updatePort(port);
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
    this.port = SocketUtils.checkPort(port);
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
    this.sendMessage(null, id, data);
  }

  /**
   * Sends all passed data with the given id to the specified host (and port)
   *
   * @param timeout Timeout for connection
   * @param id      id of data package
   * @param data    data to be sent
   * @throws IOException host not reachable or other connect exception
   */
  public void sendMessage(final int timeout, final String id, final Object... data)
      throws IOException {
    this.sendMessage(null, timeout, id, data);
  }

  /**
   * Sends all passed data with the given id to the specified host (and port)
   *
   * @param e    code to run after message was sent
   * @param id   id of data package
   * @param data data to be sent
   * @throws IOException host not reachable or other connect exception
   */
  public void sendMessage(final Executable e, final String id, final Object... data)
      throws IOException {
    this.sendMessage(e, SocketUtils.DEFAULT_TIMEOUT, id, data);
  }

  /**
   * Sends all passed data with the given id to the specified host (and port)
   *
   * @param e       code to run after message was sent
   * @param timeout Timeout for connection
   * @param id      id of data package
   * @param data    data to be sent
   * @throws IOException host not reachable or other connect exception
   */
  public void sendMessage(final Executable e, final int timeout, final String id,
                          final Object... data) throws IOException {
    final DataPackage pkg = new DataPackage(id, data);
    try (SSLSocket socket = SocketUtils.createSSLSocket(this.host, this.port, timeout)) {
      ConnectionUtils.performHandshake(socket);
      ConnectionUtils.writeObject(socket, pkg);
      if (e != null) {
        e.run(null, socket);
      }
    }
  }

  /**
   * Checks if this client can connect to the specified host
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
