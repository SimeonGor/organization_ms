package com.simeon;

import com.simeon.commands.UnknownCommandException;
import com.simeon.commands.output.OutputHandler;
import com.simeon.connection.ConnectionChannel;

import java.nio.channels.ClosedChannelException;

public class ResponseListener {
    private Thread thread;

    private final ConnectionChannel connectionChannel;
    private final OutputHandler outputHandler;
    private final CLI cli;
    public ResponseListener(ConnectionChannel connectionChannel, OutputHandler outputHandler, CLI cli) {
        this.connectionChannel =connectionChannel;
        this.outputHandler = outputHandler;
        this.cli = cli;
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void start() {
        thread = new Thread(new Receiver());
        thread.start();
    }

    private class Receiver implements Runnable {

        @Override
        public void run() {
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                Response response = connectionChannel.receive();
                if (response != null) {
                    try {
                        System.out.println(response);
                        outputHandler.handle(response, cli);
                    } catch (UnknownCommandException ignored) {
                        ;
                    }
                }
            }
        }
    }
}
