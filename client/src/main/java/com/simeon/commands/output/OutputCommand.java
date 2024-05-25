package com.simeon.commands.output;

import com.simeon.CLI;

import java.io.Serializable;

public interface OutputCommand {
    Class<? extends Serializable> getOutputType();
    void show(Serializable message, CLI cli) throws ClassCastException;
}
