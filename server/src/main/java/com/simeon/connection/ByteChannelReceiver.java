package com.simeon.connection;

import com.simeon.Request;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

@AllArgsConstructor
public class ByteChannelReceiver implements IReceiver {
    protected ByteChannel byteChannel;
    @Override
    public Request receive() {
        try {
            ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(byteChannel));
            return (Request) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
