package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Address;
import com.simeon.collection.element.Organization;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Command to print the values of the PostalAddress field of all elements in descending order
 */
public class PrintFieldDescendingPostalAddressCommand extends Command {
    public PrintFieldDescendingPostalAddressCommand(Server server) {
        super(server, "print_field_descending_postal_address", "", false,
                "print the values of the PostalAddress field of all elements in descending order");
    }

    @Override
    public void execute(Client client) {
        CollectionManager collectionManager = server.getCollectionManager();
        if (collectionManager.isEmpty()) {
            client.receiveResponse(new Response(false, "The collection is empty"));
            return;
        }

        //TODO
        client.receiveResponse(new Response(true, collectionManager.selectWhere(new Predicate<Organization>() {
            @Override
            public boolean test(Organization organization) {
                return organization.getPostalAddress() != null;
            }
        })));
    }
}
