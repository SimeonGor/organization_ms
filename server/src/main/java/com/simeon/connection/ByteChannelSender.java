package com.simeon.connection;

import com.simeon.Response;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

@AllArgsConstructor
public class ByteChannelSender implements ISender {
    protected ByteChannel byteChannel;
    @Override
    public boolean send(Response response) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(Channels.newOutputStream(byteChannel));
            out.writeObject(response);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
