package com.simeon.exceptions;

public class DataBaseRE implements RequestError {
    private final String message;

    public DataBaseRE() {
        message = "Error writing to the database";
    }
    public DataBaseRE(String cause) {
        message = String.format("Error writing to the database, because of %s.", cause);
    }

    @Override
    public String getMessage() {
        return null;
    }
}
