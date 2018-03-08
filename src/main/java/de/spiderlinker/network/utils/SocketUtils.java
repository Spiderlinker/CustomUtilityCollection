package de.spiderlinker.network.utils;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class SocketUtils {

  public static final int PORT_LIMIT = 65535; // or (1 << 16) - 1 || 0xFFFF - 1 || Short.MAX_VALUE - 1...
  public static final int DEFAULT_LIMIT = 50; // default client connection
  public static final int DEFAULT_TIMEOUT = 3000; // default time out

  /**
   * @param port
   * @return
   */
  public static int checkPort(int port) {
    if (port < 0 || port > PORT_LIMIT) {
      throw new IllegalArgumentException("Port is not valid (0<port<=" + PORT_LIMIT + ")");
    }
    return port;
  }

  /*
   * - - - - - - SSL Sockets - - - - - -
   */

  public static SSLSocket createSSLSocket(String host, int port) throws IOException {
    /* Create SSLSocket with default timeout */
    return createSSLSocket(host, port, DEFAULT_TIMEOUT);
  }

  public static SSLSocket createSSLSocket(String host, int port, int timeout)
      throws IOException {
    /* Create SSLSocket */
    SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
    socket.setEnabledCipherSuites(factory.getSupportedCipherSuites());
    socket.setSoTimeout(timeout);// set timeout of socket

    return socket;
  }

  /*
   * - - - - - - - - - - SSL Server Sockets - - - - - - - - - -
   */

  public static SSLServerSocket createSSLServerSocket(int port) throws IOException {
    /* create server with passed port and default connection limit */
    return createSSLServerSocket(port, DEFAULT_LIMIT);
  }

  public static SSLServerSocket createSSLServerSocket(int port, int limit) throws IOException {
    /* create server with passed port, limit and default (null) address */
    return createSSLServerSocket(port, limit, null);
  }

  public static SSLServerSocket createSSLServerSocket(int port, int limit, InetAddress address) throws IOException {
    /* Create SSLServerSocket */
    SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    final SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(port, limit, address);
    serverSocket.setEnabledCipherSuites(factory.getSupportedCipherSuites());

    return serverSocket;
  }

  /*
   * - - - - Socket - - - -
   */

  public static Socket createSocket(String host, int port) throws IOException {
    /* return socket */
    return createSocket(host, port, DEFAULT_TIMEOUT);
  }

  public static Socket createSocket(String host, int port, int timeout) throws IOException {
    /* create socket */
    Socket socket = SocketFactory.getDefault().createSocket(host, port);
    socket.setSoTimeout(timeout);// set timeout

    /* return socket */
    return socket;
  }

  /*
   * - - - - - - - - Server Sockets - - - - - - - -
   */

  public static ServerSocket createServerSocket(int port) throws IOException {
    /* create and return server socket with passed port and default limit */
    return createServerSocket(port, DEFAULT_LIMIT);
  }

  public static ServerSocket createServerSocket(int port, int limit) throws IOException {
    /* create and return server socket with port and limit */
    return createServerSocket(port, limit, null);
  }

  public static ServerSocket createServerSocket(int port, int limit, InetAddress address) throws IOException {
    /* create and return server socket with passed port, limit and bind to address */
    return ServerSocketFactory.getDefault().createServerSocket(port, limit, address);
  }

  /*
   * - - - - - - - - Local addresses - - - - - - - -
   */
  public static List<InetAddress> getLocalAddresses() throws UnknownHostException {
    /*
     * contains local address(es) (may be more than one, if the user has
     * installed a virtual machine. Then he could have two IPs: one of the
     * VM and the 'real' one)
     */
    List<InetAddress> address = new ArrayList<>();

    /* get local host name to receive all registered IP addresses */
    String localhost = InetAddress.getLocalHost().getHostName();
    for (InetAddress inetAddress : InetAddress.getAllByName(localhost)) {
      /* check if current address is an instance of IPv4 */
      if (inetAddress instanceof Inet4Address) {
        /* found local address, add to list */
        address.add(inetAddress);
      }
    }

    /* return found address(es) */
    return address;
  }

}
