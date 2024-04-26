package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.HashMap;
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
    public Response execute(@NonNull HashMap<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        try {
            long id = (long) parameters.get("id");
            Organization element = (Organization) parameters.get("element");
            element.setId(id);
            collectionManager.updateWhere(element, organization -> organization.getId() == id);
            return new Response(true, String.format("The item with id %s has been successful updated", id));
        } catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }

    }
}
