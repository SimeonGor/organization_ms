package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.NonNull;

import java.io.*;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.Channels;

public class BlockingConnectionChannel implements ConnectionChannel {
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public BlockingConnectionChannel(ByteChannel byteChannel) {
        try {
            this.outputStream = new ObjectOutputStream(Channels.newOutputStream(byteChannel));
            this.inputStream = new ObjectInputStream(Channels.newInputStream(byteChannel));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Request receive() {
        try {
            return (Request) inputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void send(@NonNull Response response) {
        try {
            outputStream.writeObject(response);
        }
        catch (IOException ignored) {
            ;
        }
    }

    @Override
    public void close() {
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
