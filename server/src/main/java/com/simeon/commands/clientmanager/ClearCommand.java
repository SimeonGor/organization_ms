package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Command to clearing the collection
 */
@Log
public class ClearCommand extends Command<Organization> {
    public ClearCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "clear", "clear the collection");
    }

    @Override
    public Response execute() {
        log.log(Level.FINE, "{0} command command started", name);
        collectionManager.clear();
        return new Response(true, "The collection is empty");
    }
}
