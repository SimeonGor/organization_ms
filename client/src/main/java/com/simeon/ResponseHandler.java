package com.simeon;

import lombok.NonNull;

public interface ResponseHandler {
    void handleResponse(@NonNull Response response);
}
