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
 * Command to add new item to the collection if its value exceeds the value of the largest item in this collection
 */
@Log
public class AddIfMaxCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public AddIfMaxCommand(ICollectionManager<Organization> collectionManager) {
        super("add_if_max",
                "add a new item to the collection if its value exceeds the value of the largest item in this collection");
        this.collectionManager = collectionManager;
        addParameter("element", Organization.class);
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "Add_if_max command started with ", parameters.toString());
        Organization element;
        try {
            element = (Organization) parameters.get("element");
        }
        catch (ClassCastException e ) {
            return new Response(false, e);
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "element"));
        }

        Organization added_entity = collectionManager.createIfMax(element);
        if (added_entity != null) {
            return new Response(true, "New item has been successful added to the collection");
        } else {
            return new Response(true, "New item is less than the maximum in the collection");
        }
    }
}
