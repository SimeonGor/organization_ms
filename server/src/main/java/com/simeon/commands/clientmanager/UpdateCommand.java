package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.NoSuchParameterException;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.mockito.internal.matchers.Or;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Command to update the value of a collection item whose id is equal to the specified one
 */
@Log
public class UpdateCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public UpdateCommand(ICollectionManager<Organization> collectionManager) {
        super("update",
                "update the value of a collection item whose id is equal to the specified one");
        this.collectionManager = collectionManager;
        addParameter("id", Long.class);
        addParameter("element", Organization.class);
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        long id;
        try {
            id = (long) parameters.get("id");
        } catch (ClassCastException e) {
            return new Response(false, e);
        } catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "id"));
        }
        Organization element;
        try {
            element = (Organization) parameters.get("element");
        }
        catch (ClassCastException e) {
            return new Response(false, e);
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "element"));
        }

        element.setId(id);
        Organization added_entity = collectionManager.update(element);
        return new Response(true, added_entity);
    }
}
