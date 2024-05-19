package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.NoSuchParameterException;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.Map;
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
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        long id;
        try {
            id = (long) parameters.get("id");
        }
        catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "id"));
        }

        collectionManager.deleteById(id);
        return new Response(true, String.format("The item with id %d is no longer in the collection", id));
    }
}
