package com.simeon.connection;

import com.simeon.Response;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.channels.Channels;

@AllArgsConstructor
public class Receiver implements IReceiver {
    protected InputStream inputStream;
    @Override
    public Response receive() {
        try {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
