package com.simeon.connection;

import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

public class BlockingChannelFactory implements ConnectionChannelFactory {
    @Override
    public ConnectionChannel getConnectionChannel(SocketChannel byteChannel) {
        return new BlockingConnectionChannel(byteChannel);
    }
}
