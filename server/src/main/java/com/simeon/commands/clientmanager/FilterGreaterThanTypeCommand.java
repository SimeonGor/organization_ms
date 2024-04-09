package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Command to output elements whose type field value is greater than the specified one
 */
@Log
public class FilterGreaterThanTypeCommand extends Command<Organization> {
    public FilterGreaterThanTypeCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "filter_greater_than_type",
                "output elements whose type field value is greater than the specified one");
        addParameter("type", OrganizationType.class);
    }

    @Override
    public Response execute(@NonNull HashMap<String, Object> parameters) {
        log.log(Level.FINE, "filter_greater_than_type command started with ", parameters.toString());
        try {
            OrganizationType type = (OrganizationType) parameters.get("type");
            if (collectionManager.isEmpty()) {
                return new Response(true, "The collection is empty");
            }
            return new Response(true,
                    (ArrayList<Organization>) collectionManager.getStream().filter((o1) -> o1.getType().compareTo(type) > 0).toList());
        }
        catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }
    }
}
