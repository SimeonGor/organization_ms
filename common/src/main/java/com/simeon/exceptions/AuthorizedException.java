package com.simeon.exceptions;

public class AuthorizedException extends Exception {
    public AuthorizedException() {
        super("wrong username or password");
    }
}
