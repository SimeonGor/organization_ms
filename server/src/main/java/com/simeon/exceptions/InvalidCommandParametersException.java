package com.simeon.exceptions;

public class InvalidCommandParametersException extends Exception {
    public InvalidCommandParametersException() {
        super("invalid command parameters. \nPrint \"help\" to see available commands");
    }
    public InvalidCommandParametersException(String message) {
        super(message);
    }
}
