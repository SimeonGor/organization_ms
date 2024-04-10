package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.HashMap;
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
    public Response execute(@NonNull HashMap<String, Object> parameters) {
        log.log(Level.FINE, "Add command started with ", parameters.toString());
        try {
            collectionManager.add((Organization) parameters.get("element"));
            return new Response(true, "New element has been added");
        }
        catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }
    }
}
