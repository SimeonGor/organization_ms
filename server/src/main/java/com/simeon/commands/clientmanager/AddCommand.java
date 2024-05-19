package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.DBException;
import com.simeon.exceptions.NoSuchParameterException;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Command to add new element to the collection
 */
@Log
public class AddCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public AddCommand(ICollectionManager<Organization> collectionManager) {
        super("add", "add new element to the collection");
        this.collectionManager = collectionManager;
        addParameter("element", Organization.class);
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "Add command started with ", parameters.toString());
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

        Organization entity = collectionManager.create(element);
        if (entity != null) {
            return new Response(true, entity);
        }
        else {
            return new Response(false, new DBException());
        }
    }
}
