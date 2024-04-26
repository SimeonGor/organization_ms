package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Command to reorder the collection
 */
@Log
public class ReorderCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public ReorderCommand(ICollectionManager<Organization> collectionManager) {
        super("reorder", "reorder the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "{0} command command started", name);
        if (collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        } else {
            collectionManager.reorder();
            return new Response(true, "The collection has been reordered");
        }
    }
}
