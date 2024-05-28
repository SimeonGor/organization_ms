package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.DBException;
import com.simeon.exceptions.DeniedModificationException;
import com.simeon.exceptions.NoSuchParameterException;
import com.simeon.exceptions.UnauthorizedUserException;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
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
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        if (userInfo.getRole().compareTo(Role.NO_AUTH) <= 0) {
            return new Response(false, new UnauthorizedUserException());
        }

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

        if (collectionManager.getById(id).getUserInfo().getId() == userInfo.getId()) {
            element.setId(id);
            element.setUserInfo(userInfo);
            try {
                Organization added_entity = collectionManager.update(element);
                if (added_entity != null) {
                    return new Response(true, added_entity);
                } else {
                    return new Response(false, new DBException());
                }
            }
            catch (NoSuchElementException e) {
                return new Response(false, e);
            }
        }
        else {
            return new Response(false, new DeniedModificationException());
        }
    }
}
