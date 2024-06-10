package com.simeon.commands;

import com.simeon.Response;

public class HelpCommand extends Command {
    private final CommandHandler commandHandler;
    public HelpCommand(CommandHandler commandHandler) {
        super("help", "print this help message");
        this.commandHandler = commandHandler;
    }
    @Override
    public Response execute() {
        return new Response(true, commandHandler.getCommands());
    }

}
