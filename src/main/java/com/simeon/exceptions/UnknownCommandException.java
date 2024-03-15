package com.simeon.exceptions;

/**
 * Indicates that the command name was entered incorrectly
 */
public class UnknownCommandException extends Exception {
    private final String commandName;

    public UnknownCommandException(String commandName) {
        super("command not found: " + commandName);
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
