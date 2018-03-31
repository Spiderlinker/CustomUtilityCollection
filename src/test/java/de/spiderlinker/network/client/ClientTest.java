package de.spiderlinker.network.client;

import de.spiderlinker.network.data.DataPackage;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.SSLSocket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ClientTest {

  private Client testClient;

  @Before
  public void setUp() {
    testClient = new Client();
  }

  @After
  public void cleanUp() {
    testClient = null;
  }

  @Test
  public void updateConnection() {
    String testHost = "localhost";
    int testPort = 5;
    int testTimeout = 100;

    testClient.updateConnection(testHost, testPort, testTimeout);
    Assert.assertEquals(testHost, testClient.getHost());
    Assert.assertEquals(testPort, testClient.getPort());
    Assert.assertEquals(testTimeout, testClient.getTimeout());
  }

  @Test(expected = NullPointerException.class)
  public void updateHostNull() {
    testClient.updateHost(null);
  }

  @Test(expected = NullPointerException.class)
  public void updateHostEmpty() {
    testClient.updateHost("  ");
  }

  @Test
  public void updateHostValid() {
    testClient.updateHost("localhost");
  }

  @Test(expected = IllegalArgumentException.class)
  public void updatePortOutsideLowerLimit() {
    testClient.updatePort(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void updatePortOutsideUpperLimit() {
    testClient.updatePort(65536);
  }

  @Test
  public void updateTimeoutValid() {
    int testTimeout = 1000;
    testClient.updateTimeout(testTimeout);
    Assert.assertEquals(testTimeout, testClient.getTimeout());
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateTimeoutOutsideRange() {
    testClient.updateTimeout(-1);
  }

  @Test
  public void isReachable() {
    testClient.updateHost("localhost");
    Assert.assertTrue(testClient.isReachable());
  }

  @Test
  public void isReachableNotReachable() {
    testClient.updateHost("Test");
    Assert.assertFalse(testClient.isReachable());
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