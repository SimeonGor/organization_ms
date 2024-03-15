package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Organization;
import com.simeon.collection.element.OrganizationType;

import java.util.function.Predicate;

/**
 * Command to output elements whose type field value is greater than the specified one
 */
public class FilterGreaterThanTypeCommand extends Command {
    public FilterGreaterThanTypeCommand(Server server) {
        super(server, "filter_greater_than_type", "type", false,
                "output elements whose type field value is greater than the specified one");
    }

    @Override
    public void execute(Client client) {
        OrganizationType type = NewElementBuilder.getOrganizationType(client);

        CollectionManager collectionManager = server.getCollectionManager();
        if (collectionManager.isEmpty()) {
            client.receiveResponse(new Response(false, "The collection is empty"));
            return;
        }
        client.receiveResponse(new Response(true, collectionManager.selectWhere(new Predicate<Organization>() {
            @Override
            public boolean test(Organization organization) {
                return organization.getType().compareTo(type) > 0;
            }
        })));

    }
}
