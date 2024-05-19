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
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        int index;
        try {
            index = (Integer) parameters.get("index");
        }
        catch (ClassCastException e) {
            return new Response(false, e);
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "index"));
        }

        try {
            collectionManager.deleteAt(index);
            return new Response(true,
                    String.format("The item by index %d has been successfully deleted", index));
        }
        catch (IndexOutOfBoundsException e) {
            return new Response(false, e);
        }
    }
}
