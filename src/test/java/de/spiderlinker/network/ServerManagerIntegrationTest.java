package de.spiderlinker.network;

import de.spiderlinker.network.client.Client;
import de.spiderlinker.network.data.DataPackage;
import de.spiderlinker.network.server.ServerManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerManagerIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerManagerIntegrationTest.class);

  private static final int SERVER_PORT = 56565;
  private ServerManager server;

  @Before
  public void setUp() {
    server = new ServerManager(SERVER_PORT) {
    };

    Assert.assertTrue(server.start());
  }

  @After
  public void shutdown() {
    server.stop();
  }

  @Test
  public void testHandleConnectionReadStringMessage() throws IOException {
    Client client = new Client("localhost", SERVER_PORT);
    String testIdOfMessage = "READ_STRING";
    String testString = "This is a String to test\nthe clientSocket-server communication";

    server.registerMethod(testIdOfMessage, (data, socket) -> Assert.assertEquals(testString, data.get(0)));

    LOGGER.debug("Sending message {} with id {} from {} to {}", testString, testIdOfMessage, client, server);
    client.sendMessage(testIdOfMessage, testString);
  }

  @Test
  public void testHandleConnectionReadObjectMessage() throws IOException {
    Client client = new Client("localhost", SERVER_PORT);
    String testIdOfMessage = "READ_OBJECT";

    Properties testObject = new Properties();
    testObject.put("TestKey", "TestValue");

    server.registerMethod(testIdOfMessage, (data, socket) -> Assert.assertEquals(testObject, data.get(0)));

    client.sendMessage(testIdOfMessage, testObject);
  }

  @Test
  public void testHandleConnectionMultipleClients() {
    String testIdOfMessage = "READ_OBJECT";
    Properties testObject = new Properties();
    testObject.put("TestKey", "TestValue");

    IntegerProperty receivedDataCount = new SimpleIntegerProperty();
    IntegerProperty sentDataCount = new SimpleIntegerProperty();

    server.registerMethod(testIdOfMessage, (data, socket) -> {
      receivedDataCount.set(receivedDataCount.get() + 1);
      Assert.assertEquals(testObject, data.get(0));
      System.out.println("Received from: " + data.get(1));
      try {
        server.sendMessage(socket, new DataPackage("ok", "OK"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    ThreadPoolExecutor threadService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
      final int currentIndex = i;
      threadService.submit(() -> {
        Client client = new Client("localhost", SERVER_PORT);
        try {
          System.out.println("Sending message: " + currentIndex);
          client.sendMessage(testIdOfMessage, testObject, currentIndex);
          client.receiveMessage();
          client.closeConnection();
          sentDataCount.setValue(sentDataCount.get() + 1);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }

    threadService.shutdown();
    while (!threadService.isTerminated()) {
      try {
        System.out.println("Waiting for Threads to finish...");
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    Assert.assertEquals(sentDataCount.get(), receivedDataCount.get());
  }

//  @Test
//  public void testHandleConnectionTooManyClients() {
//    String testIdOfMessage = "READ_OBJECT";
//    Properties testObject = new Properties();
//    testObject.put("TestKey", "TestValue");
//
//    int clientLimit = 60;
//    ServerManager limitedServer = new ServerManager(SERVER_PORT + 1, clientLimit) {
//    };
//    limitedServer.start();
//    BooleanProperty shouldShutdown = new SimpleBooleanProperty(false);
//    BooleanProperty clientRejected = new SimpleBooleanProperty(false);
//
//    limitedServer.registerMethod(testIdOfMessage, (data, socket) -> {
//      Assert.assertEquals(testObject, data.get(0));
//      System.out.println("Received from: " + data.get(1));
//
//      // Server is busy with first clientSocket (simulate busy state)
//      while (!shouldShutdown.get()) {
//        try {
//          System.out.println("Simulating");
//          Thread.sleep(1000);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
//    });
//
//    ThreadPoolExecutor threadService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
//    for (int i = 0; i <= clientLimit * 4; i++) {
//      final int currentIndex = i;
//      threadService.submit(() -> {
//        Client clientSocket = new Client("localhost", SERVER_PORT + 1);
//        try {
//          System.out.println("Sending message: " + currentIndex);
//          clientSocket.sendMessage(testIdOfMessage, testObject, currentIndex);
//        } catch (IOException e) {
//          e.printStackTrace();
//          if (currentIndex >= clientLimit && e.getMessage().contains("Connection refused")) {
//            clientRejected.set(true);
//          }
//        }
//      });
//    }
//
//    threadService.shutdown();
//    while (!threadService.isTerminated()) {
//      try {
//        System.out.println("Waiting for Threads to finish...");
//        Thread.sleep(200);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }
//    shouldShutdown.set(true);
//    Assert.assertTrue(clientRejected.get());
//  }


}
