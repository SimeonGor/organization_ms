package com.simeon;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Server's response
 */
@Getter
@AllArgsConstructor
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Response status
     */
    private final ResponseStatus status;
    /**
     * Response data
     */
    private final Serializable data;
}
