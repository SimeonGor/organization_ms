package com.simeon;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Server's response
 */
@Getter
@Setter
@AllArgsConstructor
public class Response implements Serializable {
    /**
     * Response status
     */
    private final boolean status;
    /**
     * Response data
     */
    private final Serializable data;
}
