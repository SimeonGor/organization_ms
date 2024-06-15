package com.simeon.connection;

import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

public interface ConnectionChannelFactory {
    ConnectionChannel getConnectionChannel(SocketChannel byteChannel);
}
