package com.simeon.connection;

import java.nio.channels.ByteChannel;

/**
 * Interface to  listen new connections
 */
public interface IListener {
    /**
     * Channel of connection
     * @return channel or null if there is not any new connections
     */
    ByteChannel accept();

    /**
     * Close listener
     */
    void close();
}
