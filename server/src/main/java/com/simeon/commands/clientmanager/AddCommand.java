package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.DataBaseRE;
import com.simeon.exceptions.NoSuchParameterRE;
import com.simeon.exceptions.UnauthorizedUserRE;
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
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo) {
        log.log(Level.INFO, "Add command started with ", parameters.toString());
        if (userInfo.getRole().compareTo(Role.USER) < 0) {
            return new Response(ResponseStatus.ERROR, new UnauthorizedUserRE());
        }

        Organization element;
        try {
            element = (Organization) parameters.get("element");
        }
        catch (NullPointerException | ClassCastException e) {
            return new Response(ResponseStatus.ERROR, new NoSuchParameterRE(name, "element"));
        }

        element.setUserInfo(userInfo);
        Organization entity = collectionManager.create(element);
        if (entity != null) {
            return new Response(ResponseStatus.ADD, entity);
        }
        else {
            return new Response(ResponseStatus.ERROR, new DataBaseRE());
        }
    }
}
