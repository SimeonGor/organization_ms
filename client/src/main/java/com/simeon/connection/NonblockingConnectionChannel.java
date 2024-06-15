package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import com.simeon.ResponseStatus;
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
    public synchronized Response receive() {
        try {
            ByteBuffer length = ByteBuffer.allocate(4);
            int r = byteChannel.read(length);
            if (r == -1) {
                return null;
            }
            int size = length.getInt(0);
            ByteBuffer result = ByteBuffer.allocate(size);
            r = 0;
            while (result.position() != size && r != -1) {
                r = byteChannel.read(result);
            }
            if (r == -1) {
                return null;
            }
            byte[] object = result.array();
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(object));
            try {
                return (Response) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                return new Response(ResponseStatus.ERROR, null);
            }
        }
        catch (IOException e) {
            return null;
        }
    }

    @Override
    public void send(@NonNull Request request) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            ByteBuffer result_buffer = ByteBuffer.allocate(byteArrayOutputStream.size() + 4);
            result_buffer.position(0);
            result_buffer.putInt(byteArrayOutputStream.toByteArray().length);
            result_buffer.put(byteArrayOutputStream.toByteArray());
            result_buffer.rewind();
            byteChannel.write(result_buffer);
        }
        catch (IOException ignored) {
            ;
        }
    }
}
