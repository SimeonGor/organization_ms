package com.simeon.commands.output;
import com.simeon.CLI;
import com.simeon.commands.input.InputCommand;
import com.simeon.commands.output.OutputCommand;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.UnknownCommandException;

import java.io.Serializable;
import java.util.HashMap;

public class OutputHandler {
    private HashMap<Class<? extends Serializable>, OutputCommand> commands = new HashMap<>();
    public boolean add(OutputCommand outputCommand) {
        if (commands.containsKey(outputCommand.getOutputType())) {
            return false;
        }
        commands.put(outputCommand.getOutputType(), outputCommand);
        return true;
    }

    public void handle(Serializable messsage, CLI cli) throws UnknownCommandException {
        if (!commands.containsKey(messsage.getClass())) {
            throw new UnknownCommandException(messsage.getClass().getCanonicalName());
        }

        OutputCommand outputCommand = commands.get(messsage.getClass());
        outputCommand.show(messsage, cli);
    }
}
