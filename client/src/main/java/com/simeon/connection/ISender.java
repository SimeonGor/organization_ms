package com.simeon.connection;

import com.simeon.Request;

/**
 * Interface to send requests
 */
public interface ISender {
    /**
     * Send request
     * @param request instance of Request
     * @return true if sending was successful
     */
    boolean send(Request request);
}