package de.spiderlinker.network.client;

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

public class AutoCloseClientTest {

  private AutoCloseClient testClient;

  @Before
  public void setUp() {
    testClient = new AutoCloseClient();
  }

  @After
  public void cleanUp() {
    testClient = null;
  }


  @Test
  public void testSendMessage() throws NoSuchFieldException, IllegalAccessException, IOException {
    SSLSocket socketMock = EasyMock.createMock(SSLSocket.class);
    EasyMock.expect(socketMock.isClosed()).andReturn(false).atLeastOnce();
    EasyMock.expect(socketMock.getOutputStream()).andReturn(new ByteArrayOutputStream()).anyTimes();
    EasyMock.expect(socketMock.getInputStream()).andReturn(new ByteArrayInputStream(new byte[1024])).anyTimes();
    // Socket will be closed
    socketMock.close();
    EasyMock.expectLastCall().once();

    EasyMock.replay(socketMock);
    changeActiveConnectionOfClient(socketMock);

    testClient.sendMessage("Test", "Data");
  }
  @Test
  public void receiveMessage() throws IOException, NoSuchFieldException, IllegalAccessException {
  }

  private void changeActiveConnectionOfClient(SSLSocket socketMock) throws IllegalAccessException, NoSuchFieldException {
    Field activeConnection = testClient.getClass().getSuperclass().getDeclaredField("activeConnection");
    activeConnection.setAccessible(true);
    activeConnection.set(testClient, socketMock);
  }


}