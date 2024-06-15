package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Response;
import com.simeon.commands.UnknownCommandException;

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

    public void handle(Response response, CLI cli) throws UnknownCommandException {
        Serializable message = response.getData();
        if (commands.containsKey(message.getClass())) {
            OutputCommand outputCommand = commands.get(message.getClass());
            try {
                outputCommand.show(response, cli);
                return;
            } catch (ClassCastException e) {
                throw new UnknownCommandException(message.getClass().getCanonicalName());
            }
        }

        for (var it : commands.entrySet()) {
            try {
                it.getValue().show(response, cli);
                return;
            } catch (ClassCastException ignored) {
            }
        }
        throw new UnknownCommandException(message.getClass().getCanonicalName());
    }
}
