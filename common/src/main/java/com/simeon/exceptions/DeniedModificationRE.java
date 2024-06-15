package com.simeon.exceptions;

public class DeniedModificationRE implements RequestError {
    private final String message;
    public DeniedModificationRE() {
        message = "access is denied for element modification";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
