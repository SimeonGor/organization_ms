package com.simeon.connection;

import java.io.OutputStream;

public class SenderFactory {
    public static ISender getSEnder(OutputStream outputStream) {
        return new NonblockingSender(outputStream);
    }
}
