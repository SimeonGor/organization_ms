package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.NonNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

public class BlockingConnectionChannel implements ConnectionChannel {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public BlockingConnectionChannel(ByteChannel byteChannel) {
        try {
            this.inputStream = new ObjectInputStream(Channels.newInputStream(byteChannel));
            this.outputStream = new ObjectOutputStream(Channels.newOutputStream(byteChannel));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Response receive() {
        try {
            return (Response) inputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void send(@NonNull Request request) {
        try {
            outputStream.writeObject(request);
        }
        catch (IOException ignored) {
            ;
        }
    }
}
