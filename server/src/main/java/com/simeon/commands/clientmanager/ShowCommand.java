package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Command to print all the elements of the collection in a string representation to the standard output stream
 */
@Log
public class ShowCommand extends Command<Organization> {
    public ShowCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "show",
                "print all the elements of the collection in a string representation to the standard output stream");
    }

    @Override
    public Response execute() {
        log.log(Level.FINE, "{0} command command started", name);
        if (collectionManager == null || collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        }
        return new Response(true, (ArrayList<Organization>) collectionManager.getStream().toList());
    }
}
