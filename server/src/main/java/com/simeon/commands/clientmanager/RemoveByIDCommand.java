package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import com.simeon.exceptions.DeniedModificationException;
import com.simeon.exceptions.NoSuchParameterException;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.mockito.internal.matchers.Or;

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
            return new Response(false, "Invalid type of parameters");
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "id"));
        }
        try {
            Organization element = collectionManager.getById(id);
            if (userInfo.getRole().compareTo(Role.ADMIN) >= 0 ||
                    element.getUserInfo().getId() == userInfo.getId()) {
                collectionManager.delete(element);
                return new Response(true, String.format("The item with id %d is no longer in the collection", id));
            } else {
                return new Response(false, new DeniedModificationException());
            }
        } catch (NoSuchElementException e) {
            return new Response(false, e);
        }
    }
}
