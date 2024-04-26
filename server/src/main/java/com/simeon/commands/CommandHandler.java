package com.simeon.commands;

import com.simeon.Response;
import com.simeon.commands.ICommand;
import com.simeon.exceptions.UnknownCommandException;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
public class CommandHandler {
    protected HashMap<String, ICommand> commands = new HashMap<>();

    public boolean addCommand(ICommand command) {
        if (commands.containsKey(command.getName())) {
            return false;
        }
        commands.put(command.getName(), command);
        return true;
    }

    public Response handle(String method, HashMap<String, ? extends Serializable> parameters) throws UnknownCommandException {
        if (commands.containsKey(method)) {
            ICommand command = commands.get(method);
            if (!parameters.isEmpty()) {
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
