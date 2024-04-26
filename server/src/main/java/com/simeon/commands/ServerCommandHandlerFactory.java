package com.simeon.commands;

import com.simeon.Server;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.servercommands.ExitCommand;
import com.simeon.commands.servercommands.SaveCommand;
import com.simeon.element.Organization;

public class ServerCommandHandlerFactory {
    public static CommandHandler getCommandHandler(ICollectionManager<Organization> collectionManager, Server server) {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand(new SaveCommand(collectionManager));
        commandHandler.addCommand(new ExitCommand(collectionManager, server));
        return commandHandler;
    }
}
