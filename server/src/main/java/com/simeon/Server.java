package com.simeon;

import com.simeon.collection.ICollectionManager;
import com.simeon.connection.Receiver;
import com.simeon.connection.Sender;
import com.simeon.connection.ServerSocketListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * Class to server
 */
@Log
@AllArgsConstructor
public class Server {
    @Getter
    private final ICollectionManager collectionManager;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3345)) {
            Socket client = serverSocket.accept();
            System.out.println("Connection accepted");
            System.out.println(client);
            Receiver receiver = new Receiver(client.getInputStream());
            Request request = receiver.receive();
            while (request == null) {
                request = receiver.receive();
            }
            System.out.println(request.getMethod());
            System.out.println(request.getParams());
            Sender sender = new Sender(client.getOutputStream());
            sender.send(new Response(true, "OK"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
