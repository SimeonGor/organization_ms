package com.simeon;

import com.simeon.authentication.AuthenticationService;
import com.simeon.authentication.IAuthenticationService;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.CommandHandler;
import com.simeon.commands.CommandHandlerFactory;
import com.simeon.commands.ServerCommandHandlerFactory;
import com.simeon.connection.ConnectionListener;
import com.simeon.connection.ConnectionLoop;
import com.simeon.connection.NonblockingConnectionChannelFactory;
import com.simeon.element.Organization;
import com.simeon.element.comparator.AnnualTurnoverComparator;
import com.simeon.exceptions.DBException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Class to server
 */
@Log
@AllArgsConstructor
public class Server {
    private final CommandHandler serverCommandHandler;
    private final ConnectionListener connectionListener;
    private final ConnectionLoop connectionLoop;

    private volatile boolean running;

    public Server(Integer port, ConnectionLoop connectionLoop) throws IOException {
        this.serverCommandHandler = ServerCommandHandlerFactory.getCommandHandler(this);
        this.connectionLoop = connectionLoop;

        this.connectionListener = new ConnectionListener(port);
    }

    public void start() {
        running = true;
        Thread server_loop = new Thread(this::loop);
        Thread connect_loop = new Thread(connectionLoop::loop);
        connect_loop.start();
        server_loop.start();
    }

    private void loop() {
        while (running) {
            try {
                SocketChannel socketChannel = connectionListener.getNewConnection();
                if (socketChannel != null) {
                    log.log(Level.INFO, "added new connection");
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
        running = false;
        connectionListener.close();
        connectionLoop.close();
        System.out.println("Server has been interrupted");
    }


    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(Server.class.getResourceAsStream("/logging.properties"));
        } catch (IOException ignored) {
            System.out.println("config file was not found");
        }

        try {
            String host = args[1];
            String database = args[2];
            int db_port = Integer.parseInt(args[3]);
            String db_user = args[4];
            String db_password = args[5];


            IAuthenticationService authenticationService = new AuthenticationService(host, database, db_port, db_user, db_password);
            ICollectionManager<Organization> collectionManager =
                    new CollectionManager(host, database, db_port, db_user, db_password, new AnnualTurnoverComparator());

            ResponseHandler responseHandler = new ResponseHandler();
            CommandHandler commandHandler = CommandHandlerFactory.getCommandHandler(collectionManager, authenticationService);
            RequestHandler requestHandler = new RequestHandler(commandHandler, responseHandler, authenticationService);

            ConnectionLoop connectionLoop = new ConnectionLoop(new NonblockingConnectionChannelFactory(), requestHandler);

            Server server = new Server(Integer.parseInt(args[0]), connectionLoop);
            System.out.println("server started");
            server.start();
        } catch (IOException e) {
            System.out.println("io exception");
        }
        catch (NumberFormatException e) {
            System.out.println("port must be an integer");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("check arguments");
        } catch (DBException e) {
            System.out.println("DB error");
        }


    }
}
