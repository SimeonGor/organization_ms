package com.simeon;

import com.simeon.connection.Receiver;
import com.simeon.connection.Sender;
import lombok.extern.java.Log;

import javax.management.ObjectName;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

/**
 * Class for client that which interacts with the user
 */
@Log
public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3345)) {
            if (!socket.isOutputShutdown()) {
                System.out.println(socket);
                Sender sender = new Sender(socket.getOutputStream());
                HashMap<String, Serializable> param = new HashMap<>();
                param.put("a", "b");
                sender.send(new Request("help", param));
                Receiver receiver = new Receiver(socket.getInputStream());
                Response response = receiver.receive();
                while (response == null) {
                    response = receiver.receive();
                }
                System.out.println(response.isStatus());
                System.out.println(response.getData());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
