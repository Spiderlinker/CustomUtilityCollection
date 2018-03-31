package de.spiderlinker.network.utils;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.Properties;

public class ConnectionUtilsTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtilsTest.class);

  @Test
  public void performHandshake() {
    Assert.fail("Not yet implemented");
  }

  @Test
  public void handleHandshake() {
    Assert.fail("Not yet implemented");
  }

  @Test
  public void readTextFromInput() throws IOException {
    ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes());

    SSLSocket socketMock = EasyMock.createMock(SSLSocket.class);
    EasyMock.expect(socketMock.getInputStream()).andReturn(inputStream).once();
    EasyMock.replay(socketMock);

    int readChar = ConnectionUtils.read(socketMock);
    LOGGER.info("Read char from ConnectionUtils#read() : {}", (char) readChar);
    Assert.assertEquals('H', readChar);
    EasyMock.verify(socketMock);
  }

  @Test(expected = NullPointerException.class)
  public void readNullSocket() throws IOException {
    ConnectionUtils.read(null);
  }

  @Test
  public void readLineTextFromInput() throws IOException {
    ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello\nWorld".getBytes());

    SSLSocket socketMock = EasyMock.createMock(SSLSocket.class);
    EasyMock.expect(socketMock.getInputStream()).andReturn(inputStream).once();
    EasyMock.replay(socketMock);

    String readString = ConnectionUtils.readLine(socketMock);
    LOGGER.info("Read String from ConnectionUtils#readLine() : {}", readString);
    Assert.assertEquals("Hello", readString);
    EasyMock.verify(socketMock);
  }

  @Test(expected = NullPointerException.class)
  public void readLineNullSocket() throws IOException {
    ConnectionUtils.readLine(null);
  }

  @Test
  public void readObject() throws IOException, ClassNotFoundException {
    Assert.fail("Not yet implemented!");
  }

  @Test
  public void print() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    SSLSocket socketMock = EasyMock.createMock(SSLSocket.class);
    EasyMock.expect(socketMock.getOutputStream()).andReturn(outputStream).once();
    EasyMock.replay(socketMock);

    String testString = "Test";
    LOGGER.info("Print String via ConnectionUtils#print() : {}", testString);
    ConnectionUtils.print(socketMock, testString);
    Assert.assertEquals(testString, outputStream.toString());
    EasyMock.verify(socketMock);
  }

  @Test
  public void println() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    SSLSocket socketMock = EasyMock.createMock(SSLSocket.class);
    EasyMock.expect(socketMock.getOutputStream()).andReturn(outputStream).once();
    EasyMock.replay(socketMock);

    String testString = "Test";
    LOGGER.info("Print String via ConnectionUtils#printLine() : {}", testString);
    ConnectionUtils.print(socketMock, testString);
    Assert.assertEquals(testString, outputStream.toString());
    EasyMock.verify(socketMock);
  }

  @Test
  public void writeObject() {
    Assert.fail("Not yet implemented");
  }

  @Test
  public void writeFile() {
    Assert.fail("Not yet implemented");
  }

  @Test
  public void readFile() {
    Assert.fail("Not yet implemented");
  }

  @Test
  public void closeMockFine() throws IOException {
    Closeable closableMock = EasyMock.createMock(Closeable.class);
    closableMock.close();
    EasyMock.expectLastCall().once();
    EasyMock.replay(closableMock);

    ConnectionUtils.close(closableMock);
    EasyMock.verify(closableMock);
  }

  @Test
  public void closeMockException() throws IOException {
    Exception exceptionMock = EasyMock.createMock(IOException.class);
    // This call might not always be the case. This depends on the
    // implementation of the used logger
    exceptionMock.printStackTrace(EasyMock.anyObject(PrintStream.class));
    EasyMock.expectLastCall().once();

    Closeable closableMock = EasyMock.createMock(Closeable.class);
    closableMock.close();
    EasyMock.expectLastCall().andThrow(exceptionMock);

    EasyMock.replay(exceptionMock, closableMock);

    ConnectionUtils.close(closableMock);
    EasyMock.verify(exceptionMock, closableMock);
  }

  @Test
  public void closeNullMock() {
    ConnectionUtils.close(null);
    // expect no exception (will be consumed)
  }

  @Test
  public void flushMockFine() throws IOException {
    Flushable flushableMock = EasyMock.createMock(Flushable.class);
    flushableMock.flush();
    EasyMock.expectLastCall().once();
    EasyMock.replay(flushableMock);

    ConnectionUtils.flush(flushableMock);
    EasyMock.verify(flushableMock);
  }

  @Test
  public void flushMockException() throws IOException {
    Exception exceptionMock = EasyMock.createMock(IOException.class);
    // This call might not always be the case. This depends on the
    // implementation of the used logger
    exceptionMock.printStackTrace(EasyMock.anyObject(PrintStream.class));
    EasyMock.expectLastCall().once();

    Flushable flushableMock = EasyMock.createMock(Flushable.class);
    flushableMock.flush();
    EasyMock.expectLastCall().andThrow(exceptionMock);

    EasyMock.replay(exceptionMock, flushableMock);

    ConnectionUtils.flush(flushableMock);
    EasyMock.verify(exceptionMock, flushableMock);
  }

  @Test
  public void flushNullMock() {
    ConnectionUtils.flush(null);
    // expect no exception (will be consumed)
  }
}