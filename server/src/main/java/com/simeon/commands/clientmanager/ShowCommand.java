package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Command to print all the elements of the collection in a string representation to the standard output stream
 */
@Log
public class ShowCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public ShowCommand(ICollectionManager<Organization> collectionManager) {
        super("show",
                "print all the elements of the collection in a string representation to the standard output stream");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "{0} command command started", name);
        if (collectionManager == null || collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        }
        return new Response(true, new ArrayList<>(collectionManager.getStream().toList()));
    }
}
