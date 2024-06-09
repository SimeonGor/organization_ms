package com.simeon;

import com.simeon.commands.*;
import com.simeon.commands.input.*;
import com.simeon.commands.output.*;
import com.simeon.connection.*;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.InvalidConnectionException;
import com.simeon.exceptions.UnknownCommandException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.LogManager;

/**
 * Class for client that which interacts with the user
 */
@Log
public class Client {
    @Setter
    @Getter
    private CLI cli;
    private CommandHandler commandHandler;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private SocketChannel socket;
    private ConnectionChannel connectionChannel;
    @Setter
    private Token token;

    public Client(CLI cli, InputHandler inputHandler, OutputHandler outputHandler) {
        this.cli = cli;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        this.token = null;

        this.outputHandler.add(new TokenOutputHandler(this));
    }

    public void start(String ip, int port) throws IOException, InvalidConnectionException {
        cli.print("Connecting...\n");
        this.socket = SocketChannel.open();
        this.socket.connect(new InetSocketAddress(ip, port));
        connect();
    }

    public void connect() throws IOException, InvalidConnectionException {
        if (socket.isConnected()) {
            connectionChannel = new NonblockingConnectionChannel(socket);
            connectionChannel.send(new Request(token, "get_api", new HashMap<>()));
            Response response = connectionChannel.receive();
            ServerCommandHandler serverCommandHandler = new ServerCommandHandler(connectionChannel);
            if (response != null && response.isStatus())
                serverCommandHandler.setCommands((ArrayList<CommandInfo>) response.getData());
            else {
                throw new InvalidConnectionException(socket.getRemoteAddress());
            }
            this.commandHandler = CommandHandlerFactory.getClientCommandHandler(serverCommandHandler, this);
        }
    }

    public void loop() {
        Scanner scanner = cli.getScanner();
        cli.printShellPrompt();
        try {
            while (scanner.hasNext()) {
                String method = scanner.next();
                try {
                    CommandInfo commandInfo = commandHandler.getCommandInfoOf(method);
                    HashMap<String, Class<? extends Serializable>> params_info = commandInfo.getParameters();
                    HashMap<String, Serializable> parameters = new HashMap<>();
                    for (var e : params_info.entrySet()) {
                        parameters.put(e.getKey(), inputHandler.getInput(e.getValue(), cli));
                    }
                    Response response = commandHandler.handle(method, parameters, token);
                    outputHandler.handle(response.getData(), cli);
                } catch (UnknownCommandException | InvalidArgumentException e) {
                    cli.error(e);
                }
                cli.flush();
                cli.printShellPrompt();
            }
        }catch (NoSuchElementException | IllegalStateException ignored) {

        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public void shutdown() throws IOException {
        close();
        cli.close();
    }
    public void send(String command, HashMap<String, ? extends Serializable> params) {

    }

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(Client.class.getResourceAsStream("/logging.properties"));
        } catch (IOException ignored) {
            System.out.println("config file was not found");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Client was interrupted");
        }));

        CLI cli = new CLI(System.in, System.out, System.out);

        Client client = new Client(cli,
                InputHandlerFactory.getInputHandler(),
                OutputHandlerFactory.getOutputHandler());

        try {
            client.start(args[0], Integer.parseInt(args[1]));
            client.loop();
        } catch (IOException e) {
            System.out.println("Something went wrong. (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
            System.out.println("Server is not available now");
        } catch (InvalidConnectionException e) {
            System.out.println(e.getMessage());
        }
    }
}
