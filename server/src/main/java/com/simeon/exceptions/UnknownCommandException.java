package com.simeon.exceptions;

import lombok.Getter;

/**
 * Indicates that the command name was entered incorrectly
 */
@Getter
public class UnknownCommandException extends Exception {
    private final String commandName;

    public UnknownCommandException(String commandName) {
        super("command not found: " + commandName);
        this.commandName = commandName;
    }

}
