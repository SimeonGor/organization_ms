package com.simeon.exceptions;

public class UnauthorizedUserException extends Exception {
    public UnauthorizedUserException() {
        super("this operation is not available to unauthorized users");
    }
}
