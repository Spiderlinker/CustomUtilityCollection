package de.spiderlinker.network;

import de.spiderlinker.network.client.Client;
import de.spiderlinker.network.data.DataPackage;
import de.spiderlinker.network.server.ServerManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerManagerIntegrationTest {

  private static final Logger LOGGER      = LoggerFactory.getLogger(ServerManagerIntegrationTest.class);
  private static final int    SERVER_PORT = 56565;

  private ThreadPoolExecutor clientThreadPool;
  private ServerManager      server;

  private Properties testData;

  @Rule
  public TestName testName = new TestName();

  @Before
  public void setUp() {
    clientThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    testData = new Properties();
    testData.put("TestKey", "TestObject");

    server = new ServerManager(SERVER_PORT) {
      @Override
      protected void onUnidentifiedMessage(DataPackage data, Socket socket) {
        super.onUnidentifiedMessage(data, socket);
      }
    };

    Assert.assertTrue(server.start());
  }

  @After
  public void shutdown() {
    server.stop();
    server = null;
  }

  @Test
  public void testHandleConnectionReadStringMessage() throws IOException {
    LOGGER.info("######################## Testing method: " + testName.getMethodName());

    Client client = new Client("localhost", SERVER_PORT);
    String testIdOfMessage = "READ_STRING";
    String testString = "This is a String to test\nthe clientSocket-server communication";

    server.registerMethod(testIdOfMessage, (data, socket) -> Assert.assertEquals(testString, data.get(0)));

    LOGGER.info("Sending message {} with id {} from {} to {}", testString, testIdOfMessage, client, server);
    client.sendMessage(testIdOfMessage, testString);
  }

  @Test
  public void testHandleConnectionReadObjectMessage() throws IOException {
    LOGGER.info("######################## Testing method: " + testName.getMethodName());

    String testIdOfMessage = "READ_OBJECT";

    registerMethodReceiveDataAndIncreaseCounter(testIdOfMessage, null);
    createClientSendMessageAndIncreaseCounter(testIdOfMessage, 0, null);

    waitUntilAllThreadsFinished();
  }

  @Test
  public void testHandleConnectionMultipleClients() {
    LOGGER.info("######################## Testing method: " + testName.getMethodName());

    String testIdOfMessage = "READ_OBJECT_MULTIPLE_CLIENTS";
    Properties testObject = new Properties();
    testObject.put("TestKey", "TestValue");

    final int dataAmount = 10;
    IntegerProperty receivedDataCount = new SimpleIntegerProperty();
    IntegerProperty sentDataCount = new SimpleIntegerProperty();

    // setup server cautious
    registerMethodReceiveDataAndIncreaseCounter(testIdOfMessage, receivedDataCount);
    for (int i = 0; i < dataAmount; i++) {
      createClientSendMessageAndIncreaseCounter(testIdOfMessage, i, sentDataCount);
    }

    waitUntilAllThreadsFinished();

    Assert.assertEquals(dataAmount, sentDataCount.get());
    Assert.assertEquals(dataAmount, receivedDataCount.get());
  }

  private void registerMethodReceiveDataAndIncreaseCounter(String idOfData, IntegerProperty counterToIncrease) {
    server.registerMethod(idOfData, (data, socket) -> {
      increaseIfExists(counterToIncrease);

      LOGGER.info("Received data {} from {}", data, socket);
      Assert.assertEquals(testData, data.get(0));

      try {
        server.sendMessage(socket, new DataPackage("OK", "Ok"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void createClientSendMessageAndIncreaseCounter(String idOfData, int dataIndex, IntegerProperty counterToIncrease) {
    submitRunnableToThreadPool(() -> {
      Client client = new Client("localhost", SERVER_PORT);
      try {
        LOGGER.info("Sending message: " + dataIndex);
        client.sendMessage(idOfData, testData, dataIndex);

        increaseIfExists(counterToIncrease);

        client.receiveMessage();
        client.closeConnection();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void submitRunnableToThreadPool(Runnable r) {
    clientThreadPool.submit(r);
  }

  private void waitUntilAllThreadsFinished() {
    clientThreadPool.shutdown();
    while (!clientThreadPool.isTerminated()) {
      try {
        LOGGER.debug("Waiting for Threads to finish...");
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private synchronized void increaseIfExists(IntegerProperty intPropToIncrease) {
    if (intPropToIncrease != null) {
      intPropToIncrease.setValue(intPropToIncrease.get() + 1);
    }
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
