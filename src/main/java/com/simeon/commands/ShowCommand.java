package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;

/**
 * Command to print all the elements of the collection in a string representation to the standard output stream
 */
public class ShowCommand extends Command {
    public ShowCommand(Server server) {
        super(server, "show", "", false,
                "print all the elements of the collection in a string representation to the standard output stream");
    }

    @Override
    public void execute(Client client) {
        CollectionManager collectionManager = server.getCollectionManager();
        if (collectionManager == null || collectionManager.isEmpty()) {
            client.receiveResponse(new Response(true, "The collection is empty"));
            return;
        }
        client.receiveResponse(new Response(true, collectionManager.getCollectionAsString()));
    }
}
