package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Client's request
 */
@Getter
@Setter
@AllArgsConstructor
public class Request implements Serializable {
    /**
     * Request's method
     */
    private final String method;
    /**
     * List of parameters
     */
    private final HashMap<String, Object> params;
}
