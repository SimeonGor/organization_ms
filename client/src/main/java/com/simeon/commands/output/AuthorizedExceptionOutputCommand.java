package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.exceptions.AuthorizedException;
import com.simeon.gui.AuthDialog;

import java.io.Serializable;

public class AuthorizedExceptionOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return AuthorizedException.class;
    }

    @Override
    public void show(Serializable message, CLI cli) throws ClassCastException {

    }
}
