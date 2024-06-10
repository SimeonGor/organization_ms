package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.exceptions.InvalidConnectionException;
import lombok.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class RestartCommand extends Command {
    protected final Client client;
    public RestartCommand(Client client) {
        super("restart", "restart connection");
        this.client = client;
        addParameter("ip", String.class);
        addParameter("port", Integer.class);
    }

    @Override
    public Response execute(@NonNull HashMap<String, ? extends Serializable> parameters) {
        try {
            client.close();
            client.start((String) parameters.get("ip"), (int) parameters.get("port"));
            return new Response(true, "reconnected");
        } catch (IOException | InvalidConnectionException e) {
            return new Response(false, e.getMessage());
        }
    }
}
