package de.spiderlinker.network.data;

import java.net.Socket;

public interface Executable {

    void run(DataPackage msg, Socket socket);

}
