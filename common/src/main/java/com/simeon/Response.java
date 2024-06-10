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
    private static final long serialVersionUID = 0L;
    /**
     * Response status
     */
    private final boolean status;
    /**
     * Response data
     */
    private final Serializable data;
}
