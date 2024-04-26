package com.simeon.commands;

import com.simeon.collection.CollectionManager;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.clientmanager.*;
import com.simeon.element.Organization;

public class CommandHandlerFactory {
    public static CommandHandler getCommandHandler(ICollectionManager<Organization> collectionManager) {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand(new AddCommand(collectionManager));
        commandHandler.addCommand(new AddIfMaxCommand(collectionManager));
        commandHandler.addCommand(new ClearCommand(collectionManager));
        commandHandler.addCommand(new FilterGreaterThanTypeCommand(collectionManager));
        commandHandler.addCommand(new InfoCommand(collectionManager));
        commandHandler.addCommand(new MinByPostalAddressCommand(collectionManager));
        commandHandler.addCommand(new PrintFieldDescendingPostalAddressCommand(collectionManager));
        commandHandler.addCommand(new RemoveAtCommand(collectionManager));
        commandHandler.addCommand(new RemoveByIDCommand(collectionManager));
        commandHandler.addCommand(new ReorderCommand(collectionManager));
        commandHandler.addCommand(new ShowCommand(collectionManager));
        commandHandler.addCommand(new UpdateCommand(collectionManager));
        commandHandler.addCommand(new GetApiCommand(commandHandler));
        return commandHandler;
    }
}
