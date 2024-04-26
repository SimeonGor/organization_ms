package com.simeon;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

public class test {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12);
        if (!socket.isOutputShutdown()) {
            HashMap<String, Serializable> params = new HashMap<>();
            for (int i = 0; i < 100; i++) {
                params.put(String.valueOf(i), (Integer) i);
            }
            params.put("haha", "hehe");

            Request request = new Request("add", params);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);

            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(byteArrayOutputStream.toByteArray().length);
            byteArrayOutputStream.writeTo(socket.getOutputStream());
            System.out.println(byteBuffer.array().length);
            System.out.println(Arrays.toString(byteBuffer.array()));
        }
        socket.close();
    }
}
