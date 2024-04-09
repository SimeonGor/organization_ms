package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;

/**
 * Interface to receive response
 */
public interface IReceiver {
    /**
     * Receive new response
     * @return response or null if there is not any requests
     */
    Response receive();
}
