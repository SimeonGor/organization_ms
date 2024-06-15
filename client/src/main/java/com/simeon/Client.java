package com.simeon;

import com.simeon.commands.CommandHandler;
import com.simeon.commands.CommandHandlerFactory;
import com.simeon.commands.ServerCommandHandler;
import com.simeon.commands.input.InputHandler;
import com.simeon.commands.input.InputHandlerFactory;
import com.simeon.commands.output.OutputHandler;
import com.simeon.commands.output.OutputHandlerFactory;
import com.simeon.commands.output.TokenOutputHandler;
import com.simeon.connection.BlockingConnectionChannel;
import com.simeon.connection.ConnectionChannel;
import com.simeon.connection.NonblockingConnectionChannel;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.InvalidConnectionException;
import com.simeon.commands.UnknownCommandException;
import com.simeon.gui.GUI;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.*;

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

    private ResponseListener responseListener;

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
        this.connectionChannel = new NonblockingConnectionChannel(socket);
        if (responseListener != null) {
            responseListener.stop();
        }
        this.responseListener = new ResponseListener(connectionChannel, outputHandler, cli);
        connect();
        SwingUtilities.invokeLater(() -> cli.setGui(new GUI(this)));
    }

    public void connect() throws IOException, InvalidConnectionException {
        if (socket.isConnected()) {
            connectionChannel.send(new Request(token, "get_api", new HashMap<>()));
            Response response = connectionChannel.receive();
            ServerCommandHandler serverCommandHandler = new ServerCommandHandler(connectionChannel);
            if (response != null && response.getStatus() == ResponseStatus.OK)
                serverCommandHandler.setCommands((ArrayList<CommandInfo>) response.getData());
            else {
                System.out.println(response);
                throw new InvalidConnectionException(socket.getRemoteAddress());
            }
            this.commandHandler = CommandHandlerFactory.getClientCommandHandler(serverCommandHandler, this);
            responseListener.start();

            send("show", new HashMap<>());
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
                    outputHandler.handle(response, cli);
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
    public void send(String method, HashMap<String, ? extends Serializable> params) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    commandHandler.handle(method, params, token);
                } catch (UnknownCommandException e) {
                    cli.error(e);
                }
            }
        });
        thread.start();
    }

    @SneakyThrows
    public static void main(String[] args) {
//        try {
//            LogManager.getLogManager().readConfiguration(Client.class.getResourceAsStream("/logging.properties"));
//        } catch (IOException ignored) {
//            System.out.println("config file was not found");
//        }
        try {
//            Properties lang = new Properties();
//            lang.load(new FileReader("setting.properties"));
//            Locale.setDefault(new Locale(lang.getProperty("lang")));
            Locale.setDefault(new Locale("ru"));
            throw new IOException();
        } catch (IOException ignored) {
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Client was interrupted");
        }));

//        CLI cli = new CLI(System.in,
//                new PrintStream(OutputStream.nullOutputStream()),
//                new PrintStream(OutputStream.nullOutputStream()));
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
