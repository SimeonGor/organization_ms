package com.simeon.connection;

import java.nio.channels.ByteChannel;

public class BlockingConnectionChannelFactory implements ConnectionChannelFactory{
    @Override
    public ConnectionChannel getConnectionChannel(ByteChannel byteChannel) {
        return new BlockingConnectionChannel(byteChannel);
    }
}
