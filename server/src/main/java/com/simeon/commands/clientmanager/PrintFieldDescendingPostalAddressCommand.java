package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;

/**
 * Command to print the values of the PostalAddress field of all elements in descending order
 */
@Log
public class PrintFieldDescendingPostalAddressCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public PrintFieldDescendingPostalAddressCommand(ICollectionManager<Organization> collectionManager) {
        super("print_field_descending_postal_address",
                "print the values of the PostalAddress field of all elements in descending order");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(@NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started", name);
        return new Response(true,
                new ArrayList<>(collectionManager.getAllItems().parallelStream()
                        .filter((o) -> o.getPostalAddress() != null)
                        .filter((o) -> o.getPostalAddress().getZipCode() != null)
                        .sorted(Comparator.comparing(o -> o.getPostalAddress().getZipCode()))
                        .toList()));
    }
}
