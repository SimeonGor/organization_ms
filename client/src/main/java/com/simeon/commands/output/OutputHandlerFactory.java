package com.simeon.commands.output;

import com.simeon.Client;

public class OutputHandlerFactory {
    public static OutputHandler getOutputHandler() {
        OutputHandler outputHandler = new OutputHandler();
        outputHandler.add(new ArrayListOutputCommand(outputHandler));
        outputHandler.add(new OrganizationOutputCommand());
        outputHandler.add(new StringOutputCommand());
        outputHandler.add(new CommandInfoOutputCommand());
        outputHandler.add(new RequestErrorOutputCommand());
        outputHandler.add(new CollectionInfoOutputCommand());
        outputHandler.add(new AuthorizationREOutputCommand());
        return outputHandler;
    }
}
