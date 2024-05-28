package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.util.Scanner;

public class IntegerInputCommand implements InputCommand {

    @Override
    public Class<? extends Serializable> getInputType() {
        return Integer.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        Scanner scanner = cli.getScanner();
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }
        else {
            throw new InvalidArgumentException("It must be an integer");
        }
    }
}
