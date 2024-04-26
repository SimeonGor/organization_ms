package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Client's request
 */
@Getter
@Setter
@AllArgsConstructor
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    /**
     * Request's method
     */
    private final String method;
    /**
     * List of parameters
     */
    private final HashMap<String, ? extends Serializable> params;
}
