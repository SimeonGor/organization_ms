package com.simeon.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

//TODO refactor
public class ServerSocketListener implements IListener {
    protected final ServerSocket serverSocket;

    public ServerSocketListener(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public int getTimeout() {
        try {
            return serverSocket.getSoTimeout();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTimeout(int timeout) {
        try {
            if (timeout > 0) {
                serverSocket.setSoTimeout(timeout);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Socket accept() {
        try {
            if (!serverSocket.isClosed()) {
                return serverSocket.accept();
            }
            return null;
        }
        catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
