package com.simeon;

import com.simeon.connection.ConnectionChannel;
import com.simeon.connection.ConnectionHandler;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Log
public class ResponseHandler {
    private final ExecutorService executorService;
    @Setter
    private ConnectionHandler connectionHandler;

    public ResponseHandler() {
        this.executorService = Executors.newCachedThreadPool();
    }
    public void send(Response response, ConnectionChannel connectionChannel) {
        executorService.execute(new Handler(response, connectionChannel));
    }

    public void close() {
        executorService.shutdown();
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
            log.log(Level.INFO, "send " + response.getStatus());
            if (response.getStatus().equals(ResponseStatus.ADD)
                    || response.getStatus().equals(ResponseStatus.DELETE)
                    || response.getStatus().equals(ResponseStatus.UPDATE)) {
                connectionHandler.sendAll(response);
            }
            else {
                connectionChannel.send(response);
            }
        }
    }
}
