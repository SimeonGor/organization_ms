package com.simeon.connection;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectionListener {
    private final ServerSocketChannel serverSocketChannel;
    public ConnectionListener(int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
    }

    public SocketChannel getNewConnection() throws IOException {
        if (serverSocketChannel.isOpen()) {
            return serverSocketChannel.accept();
        }
        else {
            return null;
        }
    }

    @SneakyThrows
    public void close() {
        serverSocketChannel.close();
    }
}
