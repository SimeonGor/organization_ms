package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Organization;

import java.util.function.Predicate;

/**
 * Command to update the value of a collection item whose id is equal to the specified one
 */
public class UpdateCommand extends Command {
    public UpdateCommand(Server server) {
        super(server, "update", "id {element}", true,
                "update the value of a collection item whose id is equal to the specified one");
    }

    @Override
    public void execute(String parameters, Client client) {
        CollectionManager collectionManager = server.getCollectionManager();

        try {
            long id = Long.parseLong(parameters);
            Organization newElement = NewElementBuilder.getNewElement(client);
            newElement.setId(id);

            collectionManager.updateWhere(new Predicate<Organization>() {
                @Override
                public boolean test(Organization organization) {
                    return organization.getId() == id;
                }
            }, newElement);

            client.receiveResponse(new Response(true, String.format("The item with id %s has been successful updated", id)));
        } catch (NumberFormatException e) {
            client.receiveResponse(new Response(false, "The id must be an integer"));
        }

    }
}
