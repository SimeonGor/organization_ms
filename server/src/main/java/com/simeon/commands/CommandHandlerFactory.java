package com.simeon.commands;

import com.simeon.Role;
import com.simeon.authentication.IAuthenticationService;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.clientmanager.*;
import com.simeon.element.Organization;

public class CommandHandlerFactory {
    public static CommandHandler getBanedCommandHandler(ICollectionManager<Organization> collectionManager) {
        CommandHandler banedCommandHandler = new CommandHandler(Role.BANED);
        banedCommandHandler.addCommand(new GetApiCommand(banedCommandHandler));
        return banedCommandHandler;
    }

    public static CommandHandler getNoauthCommandHandler(IAuthenticationService authenticationService, ICollectionManager<Organization> collectionManager) {
        CommandHandler noauthCommandHandler = new CommandHandler(Role.NO_AUTH);
        noauthCommandHandler.addCommand(new RegisterCommand(authenticationService));
        noauthCommandHandler.addCommand(new LoginCommand(authenticationService));
        noauthCommandHandler.addCommand(new GetApiCommand(noauthCommandHandler));

        return noauthCommandHandler;
    }

    public static CommandHandler gerUserCommandHandler(ICollectionManager<Organization> collectionManager) {
        CommandHandler userCommandHandler = new CommandHandler(Role.USER);
        userCommandHandler.addCommand(new AddCommand(collectionManager));
        userCommandHandler.addCommand(new AddIfMaxCommand(collectionManager));
        userCommandHandler.addCommand(new ClearCommand(collectionManager));
        userCommandHandler.addCommand(new FilterGreaterThanTypeCommand(collectionManager));
        userCommandHandler.addCommand(new InfoCommand(collectionManager));
        userCommandHandler.addCommand(new MinByPostalAddressCommand(collectionManager));
        userCommandHandler.addCommand(new PrintFieldDescendingPostalAddressCommand(collectionManager));
        userCommandHandler.addCommand(new RemoveAtCommand(collectionManager));
        userCommandHandler.addCommand(new RemoveByIDCommand(collectionManager));
        userCommandHandler.addCommand(new ReorderCommand(collectionManager));
        userCommandHandler.addCommand(new UpdateCommand(collectionManager));
        userCommandHandler.addCommand(new GetApiCommand(userCommandHandler));
        userCommandHandler.addCommand(new ShowCommand(collectionManager));

        return userCommandHandler;
    }

    public static CommandHandler getAdminCommandHandler() {
        CommandHandler adminCommandHandler = new CommandHandler(Role.ADMIN);
        adminCommandHandler.addCommand(new GetApiCommand(adminCommandHandler));
        return adminCommandHandler;
    }

    public static CommandHandler getCommandHandler(ICollectionManager<Organization> collectionManager, IAuthenticationService authenticationService) {
        CommandHandler banedCommandHandler = getBanedCommandHandler(collectionManager);

        CommandHandler noauthCommandHandler = getNoauthCommandHandler(authenticationService, collectionManager);
        noauthCommandHandler.setSuccessor(banedCommandHandler);

        CommandHandler userCommandHandler = gerUserCommandHandler(collectionManager);
        userCommandHandler.setSuccessor(noauthCommandHandler);

        CommandHandler adminCommandHandler = getAdminCommandHandler();
        adminCommandHandler.setSuccessor(userCommandHandler);

        return adminCommandHandler;
    }
}
