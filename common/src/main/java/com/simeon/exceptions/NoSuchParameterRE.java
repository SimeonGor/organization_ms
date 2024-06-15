package com.simeon.exceptions;

public class NoSuchParameterRE implements RequestError {
    private final String message;
    public NoSuchParameterRE(String command, String parameter) {
        message = String.format("%s needs a parameter with the name %s", command, parameter);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
