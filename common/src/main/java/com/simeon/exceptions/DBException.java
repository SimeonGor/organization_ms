package com.simeon.exceptions;

public class DBException extends Exception {
    public DBException() {
        super("Error writing to the database");
    }
}
