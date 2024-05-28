package com.simeon.exceptions;

import java.net.SocketAddress;

public class InvalidConnectionException extends Exception {
    public InvalidConnectionException(SocketAddress ip) {
        super(String.format("Please check the entered server address %s", ip));
    }
}
