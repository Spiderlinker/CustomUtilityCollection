package de.spiderlinker.network.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * This class provides various methods to interact with sockets. <br>
 * <br> !! Important !! <br>
 * All streams (Input + OutputStreams) of the given sockets will not be closed while calling any method (except readFile/writeFile) of this class!
 * The streams may be flushed but never closed. This would cause the socket to close the connection to the other end.
 * After calling any method in this class you have to close the socket by your own!
 *
 * @author Lindemann, Oliver
 * @since 09.03.2018
 */
public class ConnectionUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtils.class);

  /** Handshake connect request */
  public static final String HANDSHAKE_REQUEST = "HANDSHAKE_REQUEST";
  /** Handshake accepted from server */
  public static final String HANDSHAKE_ACCEPTED = "HANDSHAKE_ACCEPTED";
  /** Handshake denied from server */
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
    boolean success = false;
    try {
      ConnectionUtils.println(server, ConnectionUtils.HANDSHAKE_REQUEST);
      success = ConnectionUtils.HANDSHAKE_ACCEPTED.equals(ConnectionUtils.readLine(server));
    } catch (final IOException e) {
      LOGGER.error("Failed to perform handshake with " + server, e);
    }

    LOGGER.debug("Handshake performed with {} > {}", server, success);
    return success;
  }

  /**
   * Performs a handshake with the connected clientSocket
   *
   * @param client clientSocket to perform the handshake with
   * @return success of handshake
   */
  public static boolean handleHandshake(final Socket client) {
    boolean success = false;
    try {
      ConnectionUtils.println(client,
          ConnectionUtils.HANDSHAKE_REQUEST.equals(ConnectionUtils.readLine(client))
              ? ConnectionUtils.HANDSHAKE_ACCEPTED
              : ConnectionUtils.HANDSHAKE_DENIED);
      success = true;
    } catch (final IOException e) {
      LOGGER.error("Failed to perform handshake with " + client, e);
    }

    LOGGER.debug("Handshake performed with {} > {}", client, success);
    return success;
  }

  /*
   * - - - - - - - - - - Socket read methods - - - - - - - - - -
   */

  public static int read(final Socket socket) throws IOException {
    return getReaderForSocket(socket).read();
  }

  public static String readLine(final Socket socket) throws IOException {
    return getReaderForSocket(socket).readLine();
  }

  private static BufferedReader getReaderForSocket(Socket socket) throws IOException {
    return new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public static Object readObject(final Socket socket) throws IOException, ClassNotFoundException {
    /* Create ObjectInputStream */
    final ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
    return input.readObject();
  }

  public static void readFile(final Socket socket, final File fileLocation) throws IOException {
    /* BufferedInput- and OutputStreams to read incoming file and save it */
    // TODO socket will be closed after receiving -> autoclosable
    try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileLocation));
         BufferedInputStream input = new BufferedInputStream(socket.getInputStream())) {
      // -- Do not put Socket InputStream in try-with-resources
      // -- this will automatically close the socket and connection to clientSocket
      // >> Reply: It has to be put in try-source and the BufferedInputStream needs to be closed
      // >> otherwise the file will not be completely transferred

      pipeDataFromInputToOutput(input, output);
    }
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

  public static void writeFile(final Socket socket, final File file) throws IOException {
    /* BufferedInput- and OutputStreams to read file and send it over network */
    try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
         BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
      // -- Do not put Socket OutputStream in try-with-resources
      // -- this will automatically close the socket and connection to clientSocket
      // >> Reply: It has to be put in try-source and the BufferedOutputStream needs to be closed
      // >> otherwise the file will not be completely transferred

      pipeDataFromInputToOutput(input, output);
    }
  }

  private static void pipeDataFromInputToOutput(InputStream input, OutputStream output) throws IOException {
    final byte[] buffer = new byte[1024 * 8];
    int n;

    /* read data from input and write it to output */
    while ((n = input.read(buffer)) != -1) {
      output.write(buffer, 0, n);
    }
  }

  /**
   * Closes the stream quietly, no exception will be thrown
   *
   * @param closable Object to close
   * @param <C>      any object which implements AutoClosable
   */
  public static <C extends AutoCloseable> void close(final C closable) {
    if (closable != null) {
      try {
        closable.close();
      } catch (final Exception e) {
        LOGGER.error("Error while closing AutoClosable", e);
      }
    }
  }

  /**
   * Flushes the passed stream quietly, no exception will be thrown
   *
   * @param flushable object to flush
   * @param <F>       any object which implements Flushable
   */
  public static <F extends Flushable> void flush(final F flushable) {
    if (flushable != null) {
      try {
        flushable.flush();
      } catch (IOException e) {
        LOGGER.error("Error while flushing Flushable", e);
      }
    }
  }

}
