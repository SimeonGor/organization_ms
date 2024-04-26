package com.simeon.connection;

import com.simeon.Request;
import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.Arrays;

@AllArgsConstructor
public class NonblockingByteChannelReceiver implements IReceiver  {
    protected final ByteChannel byteChannel;
    @Override
    public Request receive() {
        try {
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
            return (Request) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
