package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class BlockingConnectionChannel implements ConnectionChannel {
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final SocketChannel socketChannel;

    public BlockingConnectionChannel(SocketChannel socketChannel) {
        System.out.printf("init connection channel");
        this.socketChannel = socketChannel;
        try {
            objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("new connection channel");
    }

    @Override
    public Request receive() {
        try {
            return (Request) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void send(@NonNull Response response) {
        objectOutputStream.writeObject(response);
    }

    @SneakyThrows
    @Override
    public void close() {
        socketChannel.close();
    }

    @Override
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }
}
