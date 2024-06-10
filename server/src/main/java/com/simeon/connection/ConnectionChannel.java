package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.NonNull;

import java.io.IOException;

public interface ConnectionChannel {
    Request receive() throws IOException;
    void send(@NonNull Response response);
    void close();
    boolean isClosed();
}
