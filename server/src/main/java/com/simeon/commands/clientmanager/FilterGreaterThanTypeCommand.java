package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.NoSuchParameterException;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Command to output elements whose type field value is greater than the specified one
 */
@Log
public class FilterGreaterThanTypeCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public FilterGreaterThanTypeCommand(ICollectionManager<Organization> collectionManager) {
        super("filter_greater_than_type",
                "output elements whose type field value is greater than the specified one");
        this.collectionManager = collectionManager;
        addParameter("type", OrganizationType.class);
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters) {
        log.log(Level.INFO, "filter_greater_than_type command started with ", parameters.toString());
        OrganizationType type;
        try {
            type = (OrganizationType) parameters.get("type");
        }
        catch (ClassCastException e) {
            return new Response(false, e);
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "type"));
        }

        return new Response(true,
                new ArrayList<>(collectionManager.getAllItems().parallelStream()
                        .filter((o1) -> o1.getType().compareTo(type) > 0)
                        .toList()));
    }
}
