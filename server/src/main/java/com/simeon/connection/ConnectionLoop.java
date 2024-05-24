package com.simeon.connection;
import com.simeon.Request;
import com.simeon.RequestHandler;
import com.simeon.Response;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionLoop {
    private final Lock lock = new ReentrantLock();
    private final Selector selector;
    private final ConnectionChannelFactory connectionChannelFactory;
    private final RequestHandler requestHandler;
    private final ForkJoinPool forkJoinPool;

    public ConnectionLoop(ConnectionChannelFactory connectionChannelFactory, RequestHandler requestHandler) throws IOException {
        this.selector = Selector.open();
        this.connectionChannelFactory = connectionChannelFactory;
        this.requestHandler = requestHandler;

        this.forkJoinPool = new ForkJoinPool();
    }

    public void addConnection(SocketChannel socketChannel) {
        this.lock.lock();
        try {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ,
                    connectionChannelFactory.getConnectionChannel(socketChannel));
        } catch (IOException ignored) {
            ;
        }
        finally {
            this.lock.unlock();
        }
    }

    public void loop() {
        while (selector.isOpen()) {
            try {
                if (selector.selectNow() != 0) {
                    Set<SelectionKey> selectionKey = selector.selectedKeys();
                    Receiver receiver = new Receiver(selectionKey);
                    forkJoinPool.invoke(receiver);


                    selectionKey.clear();
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    private class Receiver extends RecursiveTask<Integer> {
        private final Set<SelectionKey> selectionKeys;
        private final int left, right;

        public Receiver(Set<SelectionKey> selectionKeys) {
            this(selectionKeys, 0, selectionKeys.size());
        }
        public Receiver(Set<SelectionKey> selectionKeys, int left, int right) {
            this.selectionKeys = selectionKeys;
            this.left = left;
            this.right = right;
        }

        @Override
        protected Integer compute() {
            if ((right - left) > 3) {
                List<SelectionKey> keysArray = new ArrayList<>(selectionKeys);
                for (int i = left; i < right; ++i) {
                    SelectionKey key = keysArray.get(i);
                    if (key.isReadable()) {
                        ConnectionChannel connectionChannel = (ConnectionChannel) key.attachment();
                        try {
                            Request request = connectionChannel.receive();
                            requestHandler.handleRequest(request, connectionChannel);
                        } catch (IOException e) {
                            connectionChannel.send(new Response(false, "Reading exception"));
                        }
                    }
                }
                return (right - left);
            }

            int middle = (left + right) / 2;
            Receiver leftReceiver = new Receiver(selectionKeys, left, middle);
            Receiver rightReceiver = new Receiver(selectionKeys, middle, right);
            leftReceiver.fork();
            rightReceiver.fork();
            return leftReceiver.join() + rightReceiver.join();
        }
    }
}
