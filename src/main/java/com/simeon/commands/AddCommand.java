package com.simeon.commands;


import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Organization;

/**
 * Command to add new element to the collection
 */
public class AddCommand extends Command {
    public AddCommand(Server server) {
        super(server, "add", "{element}", false, "add new element to the collection");
    }

    @Override
    public void execute(Client client) {
        CollectionManager collectionManager = server.getCollectionManager();

        Organization newElement = NewElementBuilder.getNewElement(client);
        collectionManager.add(newElement);

        client.receiveResponse(new Response(true, "The item has been successful added to the collection"));
    }
}
