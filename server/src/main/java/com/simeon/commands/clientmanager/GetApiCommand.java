package com.simeon.commands.clientmanager;

import com.simeon.CommandHandler;
import com.simeon.CommandInfo;
import com.simeon.Response;
import com.simeon.commands.ICommand;

import java.util.ArrayList;
import java.util.HashMap;

public class GetApiCommand extends Command {
    protected final CommandHandler commandHandler;
    public GetApiCommand(CommandHandler commandHandler) {
        super("get_api", "get a list of available methods");
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute() {
        HashMap<String, ICommand> commands = commandHandler.getCommands();
        ArrayList<CommandInfo> commands_api = new ArrayList<>();
        for (var e : commands.entrySet()) {
            commands_api.add(
                    new CommandInfo(e.getKey(), e.getValue().getDescription(),
                            e.getValue().getParameterTypes()));
        }
        return new Response(true, commands_api);
    }
}
