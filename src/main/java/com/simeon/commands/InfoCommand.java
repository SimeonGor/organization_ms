package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;

/**
 * Command to print information about the collection
 */
public class InfoCommand extends Command {
    public InfoCommand(Server server) {
        super(server, "info", "", false,"print information about the collection");
    }

    @Override
    public void execute(Client client) {
        CollectionManager collectionManager = server.getCollectionManager();

        String result;

        if (collectionManager.isEmpty()) {
            result= "The collection is empty";
        }
        else {
            result = collectionManager.getInfo();
        }

        client.receiveResponse(new Response(true, result.toString()));
    }
}
