package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.logging.Level;


/**
 * Command to output any object from the collection whose postal Address field value is minimal
 */
@Log
public class MinByPostalAddressCommand extends Command<Organization> {
    public MinByPostalAddressCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "min_by_postal_address", "output any object from the collection whose postal Address field value is minimal");
    }

    @Override
    public Response execute() {
        log.log(Level.FINE, "{0} command command started", name);
        if (collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        }
        var op = collectionManager.getStream().min((o1, o2) -> {
            if (o1.getPostalAddress().getZipCode() == null && o2.getPostalAddress().getZipCode() == null) return 0;
            if (o2.getPostalAddress().getZipCode() == null) return -1;
            if (o1.getPostalAddress().getZipCode() == null) return 1;
            return o1.getPostalAddress().getZipCode().compareTo(
                    o2.getPostalAddress().getZipCode());
        });
        return op.map(organization -> new Response(true, organization))
                .orElseGet(() -> new Response(true, "No such elements")); // интересная замена от IDE
    }
}
