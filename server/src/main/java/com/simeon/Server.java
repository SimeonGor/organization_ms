package com.simeon;

import com.simeon.authentication.AuthenticationService;
import com.simeon.authentication.IAuthenticationService;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.CommandHandler;
import com.simeon.commands.CommandHandlerFactory;
import com.simeon.commands.ServerCommandHandlerFactory;
import com.simeon.connection.*;
import com.simeon.element.Organization;
import com.simeon.exceptions.InvalidCollectionDataException;
import com.simeon.exceptions.UnknownCommandException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Class to server
 */
@Log
@AllArgsConstructor
public class Server {
    @Getter
    private final ICollectionManager<Organization> collectionManager;
    private final IAuthenticationService authenticationService;
    private final CommandHandler commandHandler;
    private final CommandHandler serverCommandHandler;
    private final ConnectionListener connectionListener;
    private final ConnectionLoop connectionLoop;

    private volatile boolean running;

    public Server(int port, ICollectionManager<Organization> collectionManager,
                  IAuthenticationService authenticationService, CommandHandler commandHandler,
                  ConnectionLoop connectionLoop) throws InvalidCollectionDataException, IOException {
        this.collectionManager = collectionManager;
        this.authenticationService = authenticationService;
        this.commandHandler = commandHandler;
        this.serverCommandHandler = ServerCommandHandlerFactory.getCommandHandler(this);
        this.connectionLoop = connectionLoop;

        this.connectionListener = new ConnectionListener(port);
    }

    public void start() {
        running = true;
        loop();
    }

    private void loop() {
        while (running) {
            try {
                SocketChannel socketChannel = connectionListener.getNewConnection();
                if (socketChannel != null) {
                    connectionLoop.addConnection(socketChannel);
                }
            } catch (IOException ignored) {
                ;
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
                    Response response = serverCommandHandler.handle(method, new HashMap<>(),
                            new UserInfo(0, "root", Role.ADMIN));
                    System.out.println(response.getData().toString());
                }
            }
        } catch (IOException ignored) {
        }
        catch (NoSuchElementException ignored) {
            close();
        }
    }

    public void close() {
        // TODO: 23.05.2024 закрытие всех модулей
        running = false;
        System.out.println("Server has been interrupted");
    }


    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(Server.class.getResourceAsStream("/logging.properties"));
        } catch (IOException ignored) {
            System.out.println("config file was not found");
        }
    }
}
