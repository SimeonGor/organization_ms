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
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
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
    private Socket socket;

    private String ip;
    private int port;

    public Client(CLI cli, InputHandler inputHandler, OutputHandler outputHandler) {
        this.cli = cli;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public void start(String ip, int port) throws IOException, InvalidConnectionException {
        cli.print("Connecting...\n");
        this.socket = new Socket(ip, port);
        if (!socket.isOutputShutdown()) {
            ISender sender = SenderFactory.getSEnder(socket.getOutputStream());
            IReceiver receiver = ReceiverFactory.getReceiver(socket.getInputStream());

            ServerCommandHandler servercommandHandler = new ServerCommandHandler(sender, receiver);
            sender.send(new Request("get_api", new HashMap<>()));
            Response response = receiver.receive();
            if (response != null && response.isStatus())
                servercommandHandler.setCommands((ArrayList<CommandInfo>) response.getData());
            else {
                throw new InvalidConnectionException(socket.getInetAddress(), port);
            }
            ClientCommandHandler clientCommandHandler = new ClientCommandHandler();
            clientCommandHandler.setSuccessor(servercommandHandler);
            clientCommandHandler.addCommand(new HelpCommand(clientCommandHandler));
            clientCommandHandler.addCommand(new ExitCommand(this));
            clientCommandHandler.addCommand(new ExecuteScriptCommand(this));
            clientCommandHandler.addCommand(new RestartCommand(this));
            this.commandHandler = clientCommandHandler;
            cli.print("Connected!\n");
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
                    Response response = commandHandler.handle(method, parameters);
                    outputHandler.handle(response.getData(), cli);
                } catch (UnknownCommandException | InvalidArgumentException e) {
                    cli.error(e);
                }
                cli.flush();
                cli.printShellPrompt();
            }
        } catch (IllegalStateException ignored) {
        }
    }

    public void close() throws IOException {
        cli.scanner.close();
        socket.close();
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
