package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Organization;

import java.util.Comparator;

/**
 * Command to add new item to the collection if its value exceeds the value of the largest item in this collection
 */
public class AddIfMaxCommand extends Command {
    public AddIfMaxCommand(Server server) {
        super(server, "add_if_max", "{element}", false,
                "add a new item to the collection if its value exceeds the value of the largest item in this collection");
    }

    @Override
    public void execute(Client client) {
        Organization element = NewElementBuilder.getNewElement(client);
        CollectionManager collectionManager = server.getCollectionManager();
        Comparator<Organization> cmp = collectionManager.getComparator();

        if (collectionManager.isEmpty() || cmp.compare(collectionManager.maxIf(cmp), element) < 0) {
            collectionManager.add(element);

            client.receiveResponse(new Response(true, "New item has been successful added to the collection"));
        }
        else {
            client.receiveResponse(new Response(true, "New item is less than the maximum in the collection"));
        }

    }
}
