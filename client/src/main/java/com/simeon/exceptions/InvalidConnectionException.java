package com.simeon.exceptions;

import java.net.InetAddress;

public class InvalidConnectionException extends Exception {
    public InvalidConnectionException(InetAddress ip, int port) {
        super(String.format("Please check the entered server address %s:%d", ip, port));
    }
}
