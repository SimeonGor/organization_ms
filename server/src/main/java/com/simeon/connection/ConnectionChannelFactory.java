package com.simeon.connection;

import java.nio.channels.ByteChannel;

public interface ConnectionChannelFactory {
    ConnectionChannel getConnectionChannel(ByteChannel byteChannel);
}
