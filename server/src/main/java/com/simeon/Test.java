package com.simeon;

import com.simeon.connection.BlockingConnectionChannel;
import com.simeon.connection.ConnectionChannel;
import com.simeon.connection.ConnectionListener;
import com.simeon.connection.NonblockingConnectionChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Test {
    public static void main(String[] args) throws IOException {
        ConnectionListener connectionListener = new ConnectionListener(1234);
        SocketChannel socketChannel = null;
        while (socketChannel == null) {
            socketChannel = connectionListener.getNewConnection();
        }

        ConnectionChannel connectionChannel = new NonblockingConnectionChannel(socketChannel);
        Request request = connectionChannel.receive();
        while (request == null) {
            request = connectionChannel.receive();
        }
        System.out.println(request.getMethod());
    }
}
