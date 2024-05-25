package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.NonNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class NonblockingConnectionChannel implements ConnectionChannel {
    private final ByteChannel byteChannel;

    public NonblockingConnectionChannel(ByteChannel byteChannel) {
        this.byteChannel = byteChannel;
    }
    @Override
    public Request receive() throws IOException {
        ByteBuffer length = ByteBuffer.allocate(4);
        int r = byteChannel.read(length);
        if (r == -1) {
            return null;
        }
        int size = length.getInt(0);
        ByteBuffer request = ByteBuffer.allocate(size);
        r = 0;
        while (request.position() != size && r != -1) {
            r = byteChannel.read(request);
        }
        if (r == -1) {
            return null;
        }
        byte[] object = request.array();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(object));
        try {
            return (Request) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            return new Request(null, null, null);
        }
    }

    @Override
    public void send(@NonNull Response response) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            ByteBuffer response_buffer = ByteBuffer.allocate(byteArrayOutputStream.size() + 4);
            response_buffer.position(0);
            response_buffer.putInt(byteArrayOutputStream.size());
            response_buffer.put(byteArrayOutputStream.toByteArray());
            response_buffer.rewind();
            byteChannel.write(response_buffer);
        }
        catch (IOException ignored) {
            ;
        }
    }

    @Override
    public void close() {
        try {
            byteChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
