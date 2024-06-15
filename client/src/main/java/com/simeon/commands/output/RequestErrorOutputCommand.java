package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Response;
import com.simeon.exceptions.RequestError;

import java.io.Serializable;

public class RequestErrorOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return Exception.class;
    }

    @Override
    public void show(Response response, CLI cli) throws ClassCastException {
        cli.printError(((RequestError) response.getData()).getMessage());
    }
}
