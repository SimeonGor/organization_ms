package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.ArrayList;
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
        // TODO: 19.05.2024 переделать 
        return new Response(true,
                new ArrayList<>(collectionManager.getAllItems()));
    }
}
