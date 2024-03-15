package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.element.Organization;

import java.util.Comparator;


/**
 * Command to output any object from the collection whose postal Address field value is minimal
 */
public class MinByPostalAddressCommand extends Command {
    public MinByPostalAddressCommand(Server server) {
        super(server, "min_by_postal_address", "", false,
                "output any object from the collection whose postal Address field value is minimal");
    }

    @Override
    public void execute(Client client) {
        CollectionManager collectionManager = server.getCollectionManager();

        if (collectionManager.isEmpty()) {
            client.receiveResponse(new Response(false, "The collection is empty"));
            return;
        }
        client.receiveResponse(new Response(true, collectionManager.minIf(new Comparator<Organization>() {
            @Override
            public int compare(Organization o1, Organization o2) {
                if (o1.getPostalAddress().getZipCode() == null && o2.getPostalAddress().getZipCode() == null) return 0;
                if (o2.getPostalAddress().getZipCode() == null) return -1;
                if (o1.getPostalAddress().getZipCode() == null) return 1;
                return o1.getPostalAddress().getZipCode().compareTo(
                        o2.getPostalAddress().getZipCode());
            }
        }).toString()));
    }
}
