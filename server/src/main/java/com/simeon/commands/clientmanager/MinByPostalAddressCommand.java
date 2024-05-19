package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.logging.Level;


/**
 * Command to output any object from the collection whose postal Address field value is minimal
 */
@Log
public class MinByPostalAddressCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public MinByPostalAddressCommand(ICollectionManager<Organization> collectionManager) {
        super("min_by_postal_address",
                "output any object from the collection whose postal Address field value is minimal");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "{0} command command started", name);

        var op = collectionManager.getAllItems().parallelStream()
                .filter((o) -> o.getPostalAddress() != null)
                .filter((o) -> o.getPostalAddress().getZipCode() != null)
                .min(Comparator.comparing(o -> o.getPostalAddress().getZipCode()));

        return op.map(organization -> new Response(true, organization))
                .orElseGet(() -> new Response(true, "No such elements")); // интересная замена от IDE
    }
}
