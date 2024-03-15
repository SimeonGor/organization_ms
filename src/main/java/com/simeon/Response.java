package com.simeon;


import lombok.Getter;

/**
 * Class to server's response
 */
@Getter
public class Response {
    private final boolean status;
    private final String message;

    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
