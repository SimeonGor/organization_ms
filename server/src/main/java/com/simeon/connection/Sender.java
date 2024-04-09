package com.simeon.connection;

import com.simeon.Response;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Sender that use OutputStream
 * @see OutputStream
 */
@AllArgsConstructor
public class Sender implements ISender {
    protected OutputStream outputStream;

    @Override
    public boolean send(Response response) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(response);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
