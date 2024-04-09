package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

/**
 * Sender that use ByteChannel
 * @see ByteChannel
 */
@AllArgsConstructor
public class Sender implements ISender {
    protected OutputStream outputStream;

    @Override
    public boolean send(Request request) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(request);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
