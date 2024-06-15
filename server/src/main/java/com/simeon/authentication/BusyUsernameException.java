package com.simeon.authentication;

public class BusyUsernameException extends Exception {
    public BusyUsernameException(String username) {
        super(String.format("The username %s is already taken", username));
    }
}
