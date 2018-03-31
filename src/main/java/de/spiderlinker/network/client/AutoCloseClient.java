package de.spiderlinker.network.client;

import de.spiderlinker.network.data.DataPackage;

import java.io.IOException;

/**
 * This is a clientSocket which automatically closes
 * the connection after sending or receiving data.
 */
public class AutoCloseClient extends Client {

  public AutoCloseClient() {
    super();
  }

  public AutoCloseClient(String host, int port) {
    super(host, port);
  }

  public AutoCloseClient(String host, int port, int timeout) {
    super(host, port, timeout);
  }

  @Override
  public void sendMessage(DataPackage data) throws IOException {
    super.sendMessage(data);
    closeConnection();
  }

  @Override
  public DataPackage receiveMessage() throws IOException {
    DataPackage receivedData = super.receiveMessage();
    closeConnection();
    return receivedData;
  }
}
