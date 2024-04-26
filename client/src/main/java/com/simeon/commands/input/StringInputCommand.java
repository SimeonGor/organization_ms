package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.Scanner;

public class StringInputCommand implements InputCommand {
    @Override
    public Class<? extends Serializable> getInputType() {
        return String.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        Scanner scanner = cli.getScanner();
        return scanner.next();
    }
}
