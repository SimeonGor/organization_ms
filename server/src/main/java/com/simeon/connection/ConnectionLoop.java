package com.simeon.connection;

import com.simeon.Request;
import com.simeon.RequestHandler;
import com.simeon.Response;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

@Log
public class ConnectionLoop {
    private final Lock lock = new ReentrantLock();
    private volatile boolean running;
    private final ConnectionChannelFactory connectionChannelFactory;
    private final RequestHandler requestHandler;
    private final ForkJoinPool forkJoinPool;
    private final List<ConnectionChannel> connections;

    public ConnectionLoop(ConnectionChannelFactory connectionChannelFactory, RequestHandler requestHandler) throws IOException {
        this.connectionChannelFactory = connectionChannelFactory;
        this.requestHandler = requestHandler;
        this.connections = new CopyOnWriteArrayList<>();

        this.forkJoinPool = new ForkJoinPool();
    }

    public void addConnection(SocketChannel socketChannel) {
        this.lock.lock();
        try {
            connections.add(connectionChannelFactory.getConnectionChannel(socketChannel));
        } finally {
            this.lock.unlock();
        }
    }

    public void loop() {
        log.log(Level.INFO, () -> "start loop");
        running = true;
        while (running) {
            forkJoinPool.invoke(new Receiver(connections));
            for (var e : connections) {
                if (e.isClosed()) {
                    connections.remove(e);
                }
            }
        }
    }

    @SneakyThrows
    public void close() {
        running = false;
        connections.forEach(ConnectionChannel::close);
        requestHandler.close();
    }

    private class Receiver extends RecursiveTask<List<ConnectionChannel>> {
        private final List<ConnectionChannel> connectionChannels;
        private final int left, right;

        public Receiver(List<ConnectionChannel> connectionChannels) {
            this(connectionChannels, 0, connectionChannels.size());
        }
        public Receiver(List<ConnectionChannel> connectionChannels, int left, int right) {
            this.connectionChannels = connectionChannels;
            this.left = left;
            this.right = right;
        }

        @Override
        protected List<ConnectionChannel> compute() {
            if ((right - left) < 3) {
                for (int i = left; i < right; ++i) {
                    ConnectionChannel connectionChannel = connectionChannels.get(i);
                    try {
                        Request request = connectionChannel.receive();
                        if (request != null) {
                            requestHandler.handleRequest(request, connectionChannel);
                        }
                    } catch (IOException e) {
                        connectionChannel.send(new Response(false, "Reading exception"));
                    }
                }
                return null;
            }

            int middle = (left + right) / 2;
            Receiver leftReceiver = new Receiver(connectionChannels, left, middle);
            Receiver rightReceiver = new Receiver(connectionChannels, middle, right);
            leftReceiver.fork();
            rightReceiver.fork();
            return null;
        }
    }

}
