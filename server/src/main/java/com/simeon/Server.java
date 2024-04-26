package com.simeon;

import com.simeon.collection.CollectionManager;
import com.simeon.collection.ICollectionManager;
import com.simeon.collection.JsonCollectionLoader;
import com.simeon.collection.comparators.AnnualTurnoverComparator;
import com.simeon.collection.comparators.SizeComparator;
import com.simeon.commands.CommandHandler;
import com.simeon.commands.CommandHandlerFactory;
import com.simeon.commands.ServerCommandHandler;
import com.simeon.commands.ServerCommandHandlerFactory;
import com.simeon.commands.clientmanager.*;
import com.simeon.connection.*;
import com.simeon.element.Organization;
import com.simeon.exceptions.InvalidCollectionDataException;
import com.simeon.exceptions.UnknownCommandException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

import javax.validation.constraints.Min;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Class to server
 */
@Log
@AllArgsConstructor
public class Server {
    @Getter
    private ICollectionManager<Organization> collectionManager;
    private CommandHandler commandHandler;
    private CommandHandler serverCommandHandler;
    private IReceiver receiver;
    private ISender sender;
    private Selector selector;

    public Server(String filename, int port) throws InvalidCollectionDataException, IOException {
        collectionManager = new CollectionManager(filename, new JsonCollectionLoader(), new AnnualTurnoverComparator());

        this.commandHandler = CommandHandlerFactory.getCommandHandler(collectionManager);
        this.serverCommandHandler = ServerCommandHandlerFactory.getCommandHandler(collectionManager, this);

        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void loop() {
        while (selector.isOpen()) {
            try {
                if (selector.selectNow() != 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    var iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            addNewClient((ServerSocketChannel) key.channel());
                        }
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            processNewRequest(client);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                System.out.println("Something went wrong with connection");
                break;
            }

            processCLI();
        }
    }

    private void processCLI() {
        try {
            if (System.in.available() != 0) {
                Scanner scanner = new Scanner(System.in);
                String method = scanner.nextLine();
                if (!method.isEmpty()) {
                    try {
                        Response response = serverCommandHandler.handle(method, new HashMap<>());
                        System.out.println(response.getData().toString());
                    } catch (UnknownCommandException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (IOException ignored) {
            ;
        }
    }

    private void addNewClient(ServerSocketChannel serverSocketChannel) {
        try {
            SocketChannel client = serverSocketChannel.accept();
            if (client != null) {
                log.log(Level.INFO, () -> {
                    try {
                        return String.format("new connection form %s", client.getRemoteAddress());
                    } catch (IOException e) {
                        return "new unknown connectin";
                    }
                });
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ);
            }
        } catch (IOException ignored) {
            ;
        }
    }

    private void processNewRequest(SocketChannel socketChannel) {
        IReceiver receiver = new NonblockingByteChannelReceiver(socketChannel);
        ISender sender = new NonblockingByteChannelSender(socketChannel);
        Request request = receiver.receive();
        if (request != null) {
            log.log(Level.INFO, () -> {
                try {
                    return String.format("process request %s form %s", request.getMethod(), socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    return "process null";
                }
            });
            Response response;
            try {
                 response = commandHandler.handle(request.getMethod(), request.getParams());
            } catch (UnknownCommandException e) {
                response = new Response(false, e.getMessage());
            }

            boolean status = sender.send(response);
            log.log(Level.INFO, () -> {
                try {
                    return String.format("sending to %s %b", socketChannel.getRemoteAddress(), status );
                } catch (IOException e) {
                    return "sending failed";
                }
            });
        }
    }

    public void close() {
        try {
            for (var key : selector.keys()) {
                key.channel().close();
            }
            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        try {
            Server server = new Server(args[0], Integer.parseInt(args[1]));
            server.loop();
        } catch (InvalidCollectionDataException e) {
            System.out.println(e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("Port must be integer");
        } catch (IOException e) {
            System.out.println("Something went wrong with connection");
        }
    }
}
