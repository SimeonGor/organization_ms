package com.simeon.commands;

import com.simeon.CommandInfo;
import com.simeon.Response;
import com.simeon.Token;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.UnknownCommandException;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@Log
public class ClientCommandHandler extends CommandHandler {
    public HashMap<String, ICommand> commands = new HashMap<>();

    public void addCommand(ICommand command) {
        commands.put(command.getName(), command);
    }
    @Override
    public ArrayList<CommandInfo> getCommands() {
        ArrayList<CommandInfo> commandInfos = new ArrayList<>(commands.values().stream()
                .map((e) -> (new CommandInfo(e.getName(), e.getDescription(), e.getParameterTypes())))
                .toList());
        commandInfos.addAll(successor.getCommands());
        return commandInfos;
    }

    @Override
    public CommandInfo getCommandInfoOf(String method) throws UnknownCommandException {
        if (!commands.containsKey(method)) {
            return successor.getCommandInfoOf(method);
        }
        ICommand command = commands.get(method);
        return new CommandInfo(command.getName(), command.getDescription(), command.getParameterTypes());
    }

    @Override
    public Response handle(String method, HashMap<String, ? extends Serializable> parameters, Token token) throws UnknownCommandException {
        if (commands.containsKey(method)) {
            ICommand command = commands.get(method);
            try {
                if (parameters.isEmpty()) {
                    return command.execute();
                }
                else {
                    return command.execute(parameters);
                }
            } catch (InvalidArgumentException e) {
                return successor.handle(method, parameters, token);
            }
        }
        return successor.handle(method, parameters, token);
    }
}
