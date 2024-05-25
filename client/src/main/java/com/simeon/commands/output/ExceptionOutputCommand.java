package com.simeon.commands.output;

import com.simeon.CLI;

import java.io.Serializable;

public class ExceptionOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return Exception.class;
    }

    @Override
    public void show(Serializable message, CLI cli) throws ClassCastException {
        cli.error((Exception) message);
    }
}
