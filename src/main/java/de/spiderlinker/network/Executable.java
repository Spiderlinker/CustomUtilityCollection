package de.spiderlinker.network;

import java.net.Socket;

public interface Executable {

    void run(DataPackage msg, Socket socket);

}
