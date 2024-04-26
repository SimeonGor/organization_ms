package com.simeon.commands.output;

public class OutputHandlerFactory {
    public static OutputHandler getOutputHandler() {
        OutputHandler outputHandler = new OutputHandler();
        outputHandler.add(new ArrayListOutputCommand(outputHandler));
        outputHandler.add(new OrganizationOutputCommand());
        outputHandler.add(new StringOutputCommand());
        outputHandler.add(new CommandInfoOutputCommand());
        return outputHandler;
    }
}
