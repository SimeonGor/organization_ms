package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Response;

import java.io.Serializable;

public interface OutputCommand {
    Class<? extends Serializable> getOutputType();
    void show(Response response, CLI cli) throws ClassCastException;
}
