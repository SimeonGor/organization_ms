package com.simeon.commands.output;

import com.simeon.CLI;

import java.io.Serializable;

public class StringOutputCommand implements OutputCommand {

    @Override
    public Class<? extends Serializable> getOutputType() {
        return String.class;
    }

    @Override
    public void show(Serializable message, CLI cli) {
        cli.print((String) message);
        cli.print("\n");
    }
}
