package com.simeon.connection;

import com.simeon.Request;
import com.simeon.RequestHandler;
import com.simeon.Response;
import com.simeon.ResponseStatus;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

@Log
public class ConnectionHandler {
    private final Lock lock = new ReentrantLock();
    private volatile boolean running;
    private final ConnectionChannelFactory connectionChannelFactory;
    private final RequestHandler requestHandler;
    private final ExecutorService threadPool;
    private final List<ConnectionChannel> connections;

    public ConnectionHandler(ConnectionChannelFactory connectionChannelFactory, RequestHandler requestHandler) throws IOException {
        this.connectionChannelFactory = connectionChannelFactory;
        this.requestHandler = requestHandler;
        this.connections = new CopyOnWriteArrayList<>();

        this.threadPool = Executors.newWorkStealingPool();
    }

    public void addConnection(SocketChannel socketChannel) {
        this.lock.lock();
        try {
            ConnectionChannel connectionChannel = connectionChannelFactory.getConnectionChannel(socketChannel);
            connections.add(connectionChannel);
            threadPool.submit(new Receiver(connectionChannel));
        } finally {
            this.lock.unlock();
        }
    }

    public void loop() {
        log.log(Level.INFO, () -> "start loop");
        running = true;
        while (running) {
            connections.removeIf(ConnectionChannel::isClosed);
        }
    }

    public void sendAll(Response response) {
        this.lock.lock();
        try {
            for (var e : connections) {
                if (!e.isClosed()) {
                    e.send(response);
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    @SneakyThrows
    public void close() {
        running = false;
        connections.forEach(ConnectionChannel::close);
        requestHandler.close();
    }

    private class Receiver implements Runnable {
        private ConnectionChannel connectionChannel;

        public Receiver(ConnectionChannel connectionChannel) {
            this.connectionChannel = connectionChannel;
        }

        @Override
        public void run() {
            log.log(Level.INFO, "start new connection loop");
            while (!connectionChannel.isClosed()) {
                try {
                    Request request = connectionChannel.receive();
                    if (request != null) {
                        requestHandler.handleRequest(request, connectionChannel);
                    }
                } catch (IOException e) {
                    connectionChannel.send(new Response(ResponseStatus.ERROR, "Reading exception"));
                }
            }
        }
    }
}
