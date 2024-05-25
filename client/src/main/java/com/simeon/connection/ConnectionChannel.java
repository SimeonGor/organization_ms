package com.simeon.connection;

import com.simeon.Request;
import com.simeon.Response;
import lombok.NonNull;

import java.io.IOException;

public interface ConnectionChannel {
    Response receive();
    void send(@NonNull Request request);
}
