package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.Scanner;

public class LongInputCommand implements InputCommand {
    @Override
    public Class<? extends Serializable> getInputType() {
        return Long.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        Scanner scanner = cli.getScanner();
        if (scanner.hasNextLong()) {
            return scanner.nextLong();
        }
        else {
            throw new InvalidArgumentException("It must be an integer");
        }
    }
}
