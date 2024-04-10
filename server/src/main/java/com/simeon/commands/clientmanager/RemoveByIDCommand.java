package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Command to delete an item from the collection by its id
 */
@Log
public class RemoveByIDCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public RemoveByIDCommand(ICollectionManager<Organization> collectionManager) {
        super("remove_by_id", "delete an item from the collection by its id");
        this.collectionManager = collectionManager;
        addParameter("id", Long.class);
    }

    @Override
    public Response execute(@NonNull HashMap<String, Object> parameters) {
        log.log(Level.FINE, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        if (collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        }

        try {
            long id = (long) parameters.get("id");
            collectionManager.removeWhere(organization -> organization.getId() == id);
            return new Response(true, String.format("The item with id %d is no longer in the collection", id));
        }
        catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }
    }
}
