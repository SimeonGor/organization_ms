package com.simeon.connection;

import com.simeon.Request;
import lombok.AllArgsConstructor;

import java.io.*;

@AllArgsConstructor
public class NonblockingSender implements ISender {
    protected OutputStream outputStream;
    @Override
    public boolean send(Request request) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(byteArrayOutputStream.toByteArray().length);
            byteArrayOutputStream.writeTo(outputStream);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
