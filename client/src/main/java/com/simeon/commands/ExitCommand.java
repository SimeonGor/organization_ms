package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.IOException;

public class ExitCommand extends Command {
    private final Client client;
    public ExitCommand(Client client) {
        super("exit", "exit the program");
        this.client = client;
    }

    @Override
    public Response execute() {
        try {
            client.shutdown();
            return new Response(ResponseStatus.OK, "Goodbye!");
        } catch (IOException e) {
            return new Response(ResponseStatus.ERROR, "something went wrong (」°ロ°)」");
        }
    }
}
