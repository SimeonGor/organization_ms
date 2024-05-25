package com.simeon.exceptions;

public class DeniedModificationException extends Exception {
    public DeniedModificationException() {
        super("access is denied for element modification");
    }
}
