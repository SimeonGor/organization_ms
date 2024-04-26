package com.simeon.connection;

import com.simeon.Request;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.channels.Channels;

@AllArgsConstructor
public class InputStreamReceiver implements IReceiver {
    protected InputStream inputStream;
    @Override
    public Request receive() {
        try {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            return (Request) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
