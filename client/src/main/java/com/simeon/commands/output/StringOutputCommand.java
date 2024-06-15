package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Response;

import java.io.Serializable;

public class StringOutputCommand implements OutputCommand {

    @Override
    public Class<? extends Serializable> getOutputType() {
        return String.class;
    }

    @Override
    public void show(Response message, CLI cli) {
        cli.print((String) message.getData());
        cli.print("\n");
    }
}
