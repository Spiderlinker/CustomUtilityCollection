package de.spiderlinker.network.utils;

import java.io.*;
import java.net.Socket;

public class ConnectionUtils {

  /**
   * Handshake connect request
   */
  public static final String HANDSHAKE_REQUEST = "HANDSHAKE_REQUEST";

  /**
   * Handshake accepted from server
   */
  public static final String HANDSHAKE_ACCEPTED = "HANDSHAKE_ACCEPTED";

  /**
   * Handshake denied from server
   */
  public static final String HANDSHAKE_DENIED = "HANDSHAKE_DENIED";

  /*
   * - - - - - - - - - - Connection handshake - - - - - - - - - -
   */

  /**
   * Performs a handshake with the server
   *
   * @param server server to perform handshake with
   * @return success of handshake
   */
  public static boolean performHandshake(final Socket server) {
    try {
      ConnectionUtils.println(server, ConnectionUtils.HANDSHAKE_REQUEST);
      final boolean success = ConnectionUtils.HANDSHAKE_ACCEPTED
          .equals(ConnectionUtils.readLine(server));
      return success;
    } catch (final IOException e) {
    }

    return false;
  }

  /**
   * Performs a handshake with the connected client
   *
   * @param client client to perform the handshake with
   * @return success of handshake
   */
  public static boolean handleHandshake(final Socket client) {
    try {
      ConnectionUtils.println(client,
          ConnectionUtils.HANDSHAKE_REQUEST.equals(ConnectionUtils.readLine(client))
              ? ConnectionUtils.HANDSHAKE_ACCEPTED : ConnectionUtils.HANDSHAKE_DENIED);
      return true;
    } catch (final IOException e) {
    }
    return false;
  }

  /*
   * - - - - - - - - - - Socket read methods - - - - - - - - - -
   */

  public static int read(final Socket socket) throws IOException {
    /* Create InputStreamReader */
    final BufferedReader reader = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    final int msg = reader.read();// read incoming message

    /* return read message */
    return msg;
  }

  public static String readLine(final Socket socket) throws IOException {
    /* Create InputStreamReader */
    final BufferedReader reader = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    final String msg = reader.readLine();// read incoming message

    /* return read message */
    return msg;
  }

  public static Object readObject(final Socket socket) throws IOException, ClassNotFoundException {
    /* Create ObjectInputStream */
    final ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
    final Object obj = input.readObject();// read incoming object

    /* return read object */
    return obj;
  }

  /*
   * - - - - - - - - - - - Socket write methods - - - - - - - - - - -
   */

  public static void print(final Socket socket, final String msg) throws IOException {
    /* PrintWriter to print passed message */
    PrintWriter writer = null;

    try {
      /* Create PrintWriter */
      writer = new PrintWriter(socket.getOutputStream(), true);
      writer.print(msg);// print message
    } finally {
      ConnectionUtils.flush(writer);
    }
  }

  public static void println(final Socket socket, final String msg) throws IOException {
    /* PrintWriter to print passed message */
    PrintWriter writer = null;

    try {
      /* Create PrintWriter */
      writer = new PrintWriter(socket.getOutputStream(), true);
      writer.println(msg);// print message
    } finally {
      ConnectionUtils.flush(writer);
    }
  }

  /**
   * Writes the passed object to the socket
   *
   * @param socket destination host which will receive obj
   * @param obj    object to be sent
   * @throws IOException error while writing object to host
   */
  public static void writeObject(final Socket socket, final Object obj) throws IOException {
    /* ObjectOutputStream to print object */
    ObjectOutputStream output = null;

    try {
      /* Create ObjectOutputStream */
      output = new ObjectOutputStream(socket.getOutputStream());
      output.writeObject(obj);// write object
    } finally {
      ConnectionUtils.flush(output);
    }
  }

  public static void sendFile(final Socket socket, final File file) throws IOException {
    /*
     * BufferedInput- and OutputStreams to read file and send it over
     * network
     */
    try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
         BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
      /* create buffer */
      final byte[] buffer = new byte[1024 * 8];
      int n = -1;

      /* Send file while reading */
      while ((n = input.read(buffer)) != -1) {
        /* write file */
        output.write(buffer, 0, n);
      }
    }
  }

  public static void receiveFile(final Socket socket, final File fileLocation)
      throws IOException {
    /* BufferedInput- and OutputStreams to read incoming file and save it */
    try (BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
         BufferedOutputStream output = new BufferedOutputStream( // TODO socket will be closed after receiving -> autoclosable
             new FileOutputStream(fileLocation))) {

      /* create buffer */
      final byte[] buffer = new byte[1024 * 8];
      int n = -1;

      /* Send file while reading */
      while ((n = input.read(buffer)) != -1) {
        /* write file */
        output.write(buffer, 0, n);
      }
    }
  }

  /**
   * Closes the stream quietly, no exception will be thrown
   *
   * @param c object to close
   */
  public static <C extends AutoCloseable> void close(final C c) {
    if (c != null) {
      try {
        c.close();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Flushes the passed stream quietly, no exception will be thrown
   *
   * @param flushable object to flush
   */
  public static <F extends Flushable> void flush(final F flushable) {
    if (flushable != null) {
      try {
        flushable.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
