package de.spiderlinker.network.client;

import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLSocket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ClientTest {

  private Client testClient;

  @BeforeEach
  public void setUp() {
    testClient = new Client();
  }

  @AfterEach
  public void cleanUp() {
    testClient = null;
  }

  @Test
  public void updateConnection() {
    String testHost = "localhost";
    int testPort = 5;
    int testTimeout = 100;

    testClient.updateConnection(testHost, testPort, testTimeout);
    Assertions.assertEquals(testHost, testClient.getHost());
    Assertions.assertEquals(testPort, testClient.getPort());
    Assertions.assertEquals(testTimeout, testClient.getTimeout());
  }

  @Test
  public void updateHostNull() {
    Assertions.assertThrows(NullPointerException.class, () -> testClient.updateHost(null));
  }

  @Test
  public void updateHostEmpty() {
    Assertions.assertThrows(NullPointerException.class, () -> testClient.updateHost("  "));
  }

  @Test
  public void updateHostValid() {
    testClient.updateHost("localhost");
  }

  @Test
  public void updatePortOutsideLowerLimit() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> testClient.updatePort(-1));
  }

  @Test
  public void updatePortOutsideUpperLimit() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> testClient.updatePort(65536));
  }

  @Test
  public void updateTimeoutValid() {
    int testTimeout = 1000;
    testClient.updateTimeout(testTimeout);
    Assertions.assertEquals(testTimeout, testClient.getTimeout());
  }

  @Test
  public void updateTimeoutOutsideRange() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> testClient.updateTimeout(-1));
  }

  @Test
  public void isReachable() {
    testClient.updateHost("localhost");
    Assertions.assertTrue(testClient.isReachable());
  }

  @Test
  public void isReachableNotReachable() {
    testClient.updateHost("Test");
    Assertions.assertFalse(testClient.isReachable());
  }

  @Test
  public void testSendMessage() throws NoSuchFieldException, IllegalAccessException, IOException {
    SSLSocket socketMock = EasyMock.createMock(SSLSocket.class);
    EasyMock.expect(socketMock.isClosed()).andReturn(false).atLeastOnce();
    EasyMock.expect(socketMock.getOutputStream()).andReturn(new ByteArrayOutputStream()).anyTimes();
    EasyMock.expect(socketMock.getInputStream()).andReturn(new ByteArrayInputStream(new byte[1024])).anyTimes();
    EasyMock.replay(socketMock);
    changeActiveConnectionOfClient(socketMock);

    testClient.sendMessage("Test", "Data");
  }

  private void changeActiveConnectionOfClient(SSLSocket socketMock) throws IllegalAccessException, NoSuchFieldException {
    Field activeConnection = testClient.getClass().getDeclaredField("activeConnection");
    activeConnection.setAccessible(true);
    activeConnection.set(testClient, socketMock);
  }

}