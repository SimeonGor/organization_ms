package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Client;
import com.simeon.Token;
import lombok.SneakyThrows;

import java.io.Serializable;

public class TokenOutputHandler implements OutputCommand {
    private Client client;

    public TokenOutputHandler(Client client) {
        this.client = client;
    }


    @Override
    public Class<? extends Serializable> getOutputType() {
        return Token.class;
    }

    @SneakyThrows
    @Override
    public void show(Serializable message, CLI cli) throws ClassCastException {
        client.setToken((Token) message);
        client.connect();
    }
}
