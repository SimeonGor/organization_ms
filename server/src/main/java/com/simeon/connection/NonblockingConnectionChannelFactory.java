package com.simeon.connection;

import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

public class NonblockingConnectionChannelFactory implements ConnectionChannelFactory {
    @Override
    public ConnectionChannel getConnectionChannel(SocketChannel byteChannel) {
        return new NonblockingConnectionChannel(byteChannel);
    }
}
