package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;

/**
 * Command to clearing the collection
 */
public class ClearCommand extends Command {
    public ClearCommand(Server server) {
        super(server, "clear", "", false,"clear the collection");
    }

    @Override
    public void execute(Client client) {
        server.getCollectionManager().clear();
        client.receiveResponse(new Response(true, "The collection is empty"));
    }
}
