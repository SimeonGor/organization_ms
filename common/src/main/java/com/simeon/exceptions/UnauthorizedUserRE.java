package com.simeon.exceptions;

public class UnauthorizedUserRE implements RequestError {
    private final String message;
    public UnauthorizedUserRE() {
        message = "this operation is not available to unauthorized users";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
