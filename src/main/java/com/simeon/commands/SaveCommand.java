package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;

import java.io.IOException;

/**
 * Command to save the collection to a file
 */
public class SaveCommand extends Command {
    public SaveCommand(Server server) {
        super(server, "save", "", false, "save the collection to a file");
    }

    @Override
    public void execute(Client client) {
        try {
            server.getCollectionManager().save();
            client.receiveResponse(new Response(true, "The collection has been successfully saved"));
        }
        catch (IOException e) {
            client.receiveResponse(new Response(false, e.getMessage()));
        }
    }
}
