package com.simeon.exceptions;

public class NoSuchParameterException extends Exception {
    public NoSuchParameterException(String command, String parameter) {
        super(String.format("%s needs a parameter with the name %s", command, parameter));
    }
}
