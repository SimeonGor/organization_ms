package com.simeon.connection;

import java.io.InputStream;

public class ReceiverFactory {
    public static IReceiver getReceiver(InputStream inputStream) {
        return new Receiver(inputStream);
    }
}
