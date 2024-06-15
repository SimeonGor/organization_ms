package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Token;
import lombok.SneakyThrows;

import java.io.Serializable;

public class TokenOutputHandler implements OutputCommand {
    private final Client client;

    public TokenOutputHandler(Client client) {
        this.client = client;
    }


    @Override
    public Class<? extends Serializable> getOutputType() {
        return Token.class;
    }

    @SneakyThrows
    @Override
    public void show(Response response, CLI cli) throws ClassCastException {
        Token token = (Token) response.getData();
        client.setToken(token);
        client.connect();
        cli.okAuth(token.getUsername());
    }
}
