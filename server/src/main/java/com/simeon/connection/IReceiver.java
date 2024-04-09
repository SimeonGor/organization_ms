package com.simeon.connection;

import com.simeon.Request;

/**
 * Interface to receive requests
 */
public interface IReceiver {
    /**
     * Receive new request
     * @return request or null if there is not any requests
     */
    Request receive();
}
