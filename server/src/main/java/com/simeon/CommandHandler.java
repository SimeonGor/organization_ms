package com.simeon;

import com.simeon.commands.ICommand;
import com.simeon.exceptions.UnknownCommandException;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class CommandHandler {
    protected HashMap<String, ICommand> commands;

    public boolean addCommand(ICommand command) {
        if (commands.containsKey(command.getName())) {
            return false;
        }
        commands.put(command.getName(), command);
        return true;
    }

    public Response execute(String method, HashMap<String, Object> parameters) throws UnknownCommandException {
        if (commands.containsKey(method)) {
            ICommand command = commands.get(method);
            if (parameters != null) {
                return command.execute(parameters);
            }
            else {
                return command.execute();
            }
        }
        else {
            throw new UnknownCommandException(method);
        }
    }
}
