package com.simeon.exceptions;

public class InvalidCollectionDataException extends Exception {
    public InvalidCollectionDataException(String path) {
        super(String.format("Invalid collection data in the file %s", path));
    }
}
