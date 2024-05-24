package com.simeon.commands;

import com.simeon.Role;
import com.simeon.Server;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.servercommands.ExitCommand;
import com.simeon.element.Organization;

public class ServerCommandHandlerFactory {
    public static CommandHandler getCommandHandler(Server server) {
        CommandHandler commandHandler = new CommandHandler(Role.ADMIN);
        commandHandler.addCommand(new ExitCommand(server));
        return commandHandler;
    }
}
