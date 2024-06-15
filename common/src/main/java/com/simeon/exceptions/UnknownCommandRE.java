package com.simeon.exceptions;

public class UnknownCommandRE implements RequestError{
    private final String commandName;

    public UnknownCommandRE(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public String getMessage() {
        return "command not found: " + commandName;
    }
}
