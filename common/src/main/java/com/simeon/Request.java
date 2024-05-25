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
@AllArgsConstructor
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private final Token userToken;
    /**
     * Request's method
     */
    private final String method;
    /**
     * List of parameters
     */
    private final HashMap<String, ? extends Serializable> params;
}
