package de.spiderlinker.network.data;

import javax.net.ssl.SSLSocket;

public interface Executable {

    void run(DataPackage msg, SSLSocket socket);

}
