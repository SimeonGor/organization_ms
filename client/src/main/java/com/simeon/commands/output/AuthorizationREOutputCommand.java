package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Response;
import com.simeon.exceptions.AuthorizationRE;
import com.simeon.exceptions.RequestError;

import java.io.Serializable;

public class AuthorizationREOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return AuthorizationRE.class;
    }

    @Override
    public void show(Response response, CLI cli) throws ClassCastException {
        cli.errorAuth();
        cli.printError(((RequestError) response.getData()).getMessage());
    }
}
