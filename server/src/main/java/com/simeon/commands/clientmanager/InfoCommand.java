package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Command to print information about the collection
 */
@Log
public class InfoCommand extends Command<Organization> {
    public InfoCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "info", "print information about the collection");
    }

    @Override
    public Response execute() {
        log.log(Level.FINE, "{0} command command started", name);
        String result;
        if (collectionManager.isEmpty()) {
            result= "The collection is empty";
        }
        else {
            result = collectionManager.getInfo();
        }
        return new Response(true, result);
    }
}
