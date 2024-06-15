package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.CommandInfo;
import com.simeon.Response;

import java.io.Serializable;

public class CommandInfoOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return CommandInfo.class;
    }

    @Override
    public void show(Response response, CLI cli) {
            CommandInfo commandInfo = (CommandInfo) response.getData();
            cli.print(String.format("\u001B[1m%30s\u001B[0m \u001B[3m%15s\u001B[0m %s \n",
                    commandInfo.getName(),
                    String.join(",", commandInfo.getParameters().keySet()),
                    commandInfo.getDescription()));
    }
}
