package com.simeon;

import com.simeon.connection.ConnectionChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResponseHandler {
    private final ExecutorService executorService;

    public ResponseHandler() {
        this.executorService = Executors.newCachedThreadPool();
    }
    public void send(Response response, ConnectionChannel connectionChannel) {
        executorService.execute(new Handler(response, connectionChannel));
    }

    private class Handler implements Runnable {
        private final Response response;
        private final ConnectionChannel connectionChannel;

        public Handler(Response response, ConnectionChannel connectionChannel) {
            this.response = response;
            this.connectionChannel = connectionChannel;
        }

        @Override
        public void run() {
            connectionChannel.send(response);
        }
    }
}
