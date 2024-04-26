package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.exceptions.InvalidArgumentException;
import lombok.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class ExitCommand extends Command {
    private Client client;
    public ExitCommand(Client client) {
        super("exit", "exit the program");
        this.client = client;
    }

    @Override
    public Response execute() throws InvalidArgumentException {
        try {
            client.close();
            return new Response(true, "Goodbye!");
        } catch (IOException e) {
            return new Response(false, "something went wrong (」°ロ°)」");
        }
    }
}
