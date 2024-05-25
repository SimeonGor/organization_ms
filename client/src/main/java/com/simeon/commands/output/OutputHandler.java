package com.simeon.commands.output;
import com.simeon.CLI;
import com.simeon.commands.input.InputCommand;
import com.simeon.commands.output.OutputCommand;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.UnknownCommandException;

import java.io.Serializable;
import java.util.HashMap;

public class OutputHandler {
    private final HashMap<Class<? extends Serializable>, OutputCommand> commands = new HashMap<>();
    public boolean add(OutputCommand outputCommand) {
        if (commands.containsKey(outputCommand.getOutputType())) {
            return false;
        }
        commands.put(outputCommand.getOutputType(), outputCommand);
        return true;
    }

    public void handle(Serializable message, CLI cli) throws UnknownCommandException {
        if (commands.containsKey(message.getClass())) {
            OutputCommand outputCommand = commands.get(message.getClass());
            try {
                outputCommand.show(message, cli);
                return;
            } catch (ClassCastException e) {
                throw new UnknownCommandException(message.getClass().getCanonicalName());
            }
        }

        for (var it : commands.entrySet()) {
            try {
                it.getValue().show(message, cli);
                return;
            } catch (ClassCastException ignored) {
                ;
            }
        }
        throw new UnknownCommandException(message.getClass().getCanonicalName());
    }
}
