package com.simeon.connection;

import java.net.Socket;

/**
 * Interface to  listen new connections
 */
public interface IListener {
    /**
     * Channel of connection
     * @return channel or null if there is not any new connections
     */
    Socket accept();

    /**
     * Close listener
     */
    void close();
}
