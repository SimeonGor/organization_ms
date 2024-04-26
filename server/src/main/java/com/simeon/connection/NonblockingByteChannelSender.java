package com.simeon.connection;

import com.simeon.Response;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.Arrays;

@AllArgsConstructor
public class NonblockingByteChannelSender implements ISender {
    protected final ByteChannel byteChannel;

    @Override
    public boolean send(Response response) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            ByteBuffer response_buffer = ByteBuffer.allocate(byteArrayOutputStream.size());
            response_buffer.put(byteArrayOutputStream.toByteArray());
            response_buffer.position(0);
            byteChannel.write(response_buffer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
