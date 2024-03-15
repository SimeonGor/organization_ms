package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Organization;

import java.util.function.Predicate;

/**
 * Command to delete an item from the collection by its id
 */
public class RemoveByIDCommand extends Command {
    public RemoveByIDCommand(Server server) {
        super(server, "remove_by_id", "id", true, "delete an item from the collection by its id");
    }

    public int getParameters(String param) throws NumberFormatException{
        return Integer.parseInt(param);
    }

    @Override
    public void execute(String parameters, Client client) {
        CollectionManager collectionManager = server.getCollectionManager();
        if (collectionManager.isEmpty()) {
            client.receiveResponse(new Response(false, "The collection is empty"));
            return;
        }

        try {
            long id = getParameters(parameters);
            collectionManager.removeWhere(new Predicate<Organization>() {
                @Override
                public boolean test(Organization organization) {
                    return organization.getId() == id;
                }
            });
            client.receiveResponse(new Response(true, String.format("The item with id %d is no longer in the collection", id)));
        }
        catch (NumberFormatException e) {
            client.receiveResponse(new Response(false, "The id must be an integer"));
        }
        catch (NullPointerException e) {
            client.receiveResponse(new Response(false, "The collection is empty"));
        }
    }
}
