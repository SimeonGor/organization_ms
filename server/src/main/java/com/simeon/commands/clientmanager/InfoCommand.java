package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Command to print information about the collection
 */
@Log
public class InfoCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public InfoCommand(ICollectionManager<Organization> collectionManager) {
        super("info", "print information about the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "{0} command command started", name);
        return new Response(true, collectionManager.getInfo());
    }
}
