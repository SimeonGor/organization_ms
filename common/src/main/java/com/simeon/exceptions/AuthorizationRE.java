package com.simeon.exceptions;

public class AuthorizationRE implements RequestError {
    public final String message;

    public AuthorizationRE(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
