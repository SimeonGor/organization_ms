package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;

/**
 * Command to reorder the collection
 */
public class ReorderCommand extends Command {
    public ReorderCommand(Server server) {
        super(server, "reorder", "", false, "reorder the collection");
    }

    @Override
    public void execute(Client client) {
        CollectionManager collectionManager = server.getCollectionManager();

        if (collectionManager.isEmpty()) {
            client.receiveResponse(new Response(false, "The collection is empty"));
        } else {
            collectionManager.reorder();
            client.receiveResponse(new Response(true, "The collection has been reordered"));
        }
    }
}
