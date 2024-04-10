package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Command to delete an item from the collection by index
 */
@Log
public class RemoveAtCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public RemoveAtCommand(ICollectionManager<Organization> collectionManager) {
        super("remove_at","delete an item from the collection by index");
        addParameter("index", Integer.class);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(@NonNull HashMap<String, Object> parameters) {
        log.log(Level.FINE, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        if (collectionManager.isEmpty()) {
            return new Response(true, "The collection is empty");
        }

        try {
            int index = (Integer) parameters.get("index");
            boolean status = collectionManager.removeAt(index - 1);
            if (status) {
                return new Response(true,
                        String.format("The item by index %d has been successfully deleted", index));
            } else {
                return new Response(false,
                        String.format("The index must be in the range from 1 to %s",
                                collectionManager.size()));
            }
        }
        catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }
    }
}
