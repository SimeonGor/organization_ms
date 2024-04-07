package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.logging.Level;

/**
 * Command to print the values of the PostalAddress field of all elements in descending order
 */
@Log
public class PrintFieldDescendingPostalAddressCommand extends Command<Organization> {
    public PrintFieldDescendingPostalAddressCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "print_field_descending_postal_address",
                "print the values of the PostalAddress field of all elements in descending order");
    }

    @Override
    public Response execute() {
        log.log(Level.FINE, "{0} command command started", name);
        if (collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        }
        return new Response(true,
                collectionManager.getStream()
                        .filter((o) -> o.getPostalAddress() != null && o.getPostalAddress().getZipCode() != null)
                        .sorted(Comparator.comparing(o -> o.getPostalAddress().getZipCode()))
                        .toList());
    }
}
