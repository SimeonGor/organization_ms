package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.NoSuchParameterRE;
import com.simeon.exceptions.UnauthorizedUserRE;
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
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo) {
        log.log(Level.INFO, "Add_if_max command started with ", parameters.toString());
        if (userInfo.getRole().compareTo(Role.USER) < 0) {
            return new Response(ResponseStatus.ERROR, new UnauthorizedUserRE());
        }

        Organization element;
        try {
            element = (Organization) parameters.get("element");
        }
        catch (NullPointerException | ClassCastException e ) {
            return new Response(ResponseStatus.ERROR, new NoSuchParameterRE(name, "element"));
        }

        element.setUserInfo(userInfo);
        Organization added_entity = collectionManager.createIfMax(element);
        if (added_entity != null) {
            return new Response(ResponseStatus.ADD, "New item has been successful added to the collection");
        } else {
            return new Response(ResponseStatus.OK, "New item is less than the maximum in the collection");
        }
    }
}
