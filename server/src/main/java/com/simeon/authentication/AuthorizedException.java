package com.simeon.authentication;

public class AuthorizedException extends Exception {
    public AuthorizedException() {
        super("wrong username or password");
    }
}
