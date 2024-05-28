package com.simeon.commands;

import com.simeon.Client;

public class CommandHandlerFactory {
    public static CommandHandler getClientCommandHandler(CommandHandler serverCommandHandler, Client client) {
        ClientCommandHandler clientCommandHandler = new ClientCommandHandler();
        clientCommandHandler.setSuccessor(serverCommandHandler);
        clientCommandHandler.addCommand(new HelpCommand(clientCommandHandler));
        clientCommandHandler.addCommand(new ExitCommand(client));
        clientCommandHandler.addCommand(new ExecuteScriptCommand(client));
        clientCommandHandler.addCommand(new RestartCommand(client));

        return clientCommandHandler;
    }

}
