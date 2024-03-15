package com.simeon.exceptions;


import lombok.NoArgsConstructor;

/**
 * Indicates that the method argument does not fit the range
 */
public class InvalidArgument extends Exception {
    public InvalidArgument(String message) {
        super(message);
    }
}
