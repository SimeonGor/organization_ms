package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.commands.input.InputCommand;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.UnknownCommandException;

import java.io.Serializable;
import java.util.HashMap;

public class InputHandler {
    private HashMap<Class<? extends Serializable>, InputCommand> commands = new HashMap<>();

    public boolean add(InputCommand inputCommand) {
        if (commands.containsKey(inputCommand.getInputType())) {
            return false;
        }
        commands.put(inputCommand.getInputType(), inputCommand);
        return true;
    }

    public Serializable getInput(Class<? extends Serializable> type, CLI cli) throws UnknownCommandException, InvalidArgumentException {
        if (!commands.containsKey(type)) {
            throw new UnknownCommandException(type.getCanonicalName());
        }
        InputCommand inputCommand = commands.get(type);
        return inputCommand.read(cli);
    }
}
