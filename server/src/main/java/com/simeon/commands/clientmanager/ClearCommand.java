package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Command to clearing the collection
 */
@Log
public class ClearCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public ClearCommand(ICollectionManager<Organization> collectionManager) {
        super("clear", "clear the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "{0} command command started", name);
        collectionManager.clear();
        return new Response(true, "The collection is empty");
    }
}
