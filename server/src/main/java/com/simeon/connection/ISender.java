package com.simeon.connection;

import com.simeon.Response;

/**
 * Interface to send responses
 */
public interface ISender {
    /**
     * Send response
     * @param response instance of response
     * @return true if sending was successful
     */
    boolean send(Response response);
}
