package com.simeon.commands.clientmanager;

import com.simeon.CommandInfo;
import com.simeon.Response;
import com.simeon.commands.Command;
import com.simeon.commands.CommandHandler;

import java.util.ArrayList;

public class GetApiCommand extends Command {
    protected final CommandHandler commandHandler;
    public GetApiCommand(CommandHandler commandHandler) {
        super("get_api", "");
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute() {
        return new Response(true,
                new ArrayList<>(commandHandler.getCommands().entrySet().stream()
                        .filter((entry) -> entry.getValue() != this)
                        .map((entry) -> new CommandInfo(
                                entry.getKey(),
                                entry.getValue().getDescription(),
                                entry.getValue().getParameterTypes()))
                        .toList()));
    }
}
