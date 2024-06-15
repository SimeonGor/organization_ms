package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.DeniedModificationRE;
import com.simeon.exceptions.NoSuchParameterRE;
import com.simeon.exceptions.RequestError;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;

/**
 * Command to delete an item from the collection by its id
 */
@Log
public class RemoveByIDCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public RemoveByIDCommand(ICollectionManager<Organization> collectionManager) {
        super("remove_by_id", "delete an item from the collection by its id");
        this.collectionManager = collectionManager;
        addParameter("id", Long.class);
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        long id;
        try {
            id = (long) parameters.get("id");
        }
        catch (ClassCastException e) {
            return new Response(ResponseStatus.ERROR, new NoSuchParameterRE(name, "id"));
        }

        try {
            Organization element = collectionManager.getById(id);
            if (userInfo.getRole().compareTo(Role.ADMIN) >= 0 ||
                    element.getUserInfo().getId() == userInfo.getId()) {
                collectionManager.delete(element);
                return new Response(ResponseStatus.DELETE, element);
            } else {
                return new Response(ResponseStatus.ERROR, new DeniedModificationRE());
            }
        } catch (NoSuchElementException e) {
            return new Response(ResponseStatus.ERROR, new RequestError() {
                @Override
                public String getMessage() {
                    return "No such element";
                }
            });
        }
    }
}
