package com.simeon.connection;

import java.nio.channels.ByteChannel;

public class NonblockingConnectionChannelFactory implements ConnectionChannelFactory {
    @Override
    public ConnectionChannel getConnectionChannel(ByteChannel byteChannel) {
        return new NonblockingConnectionChannel(byteChannel);
    }
}
