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
import java.util.logging.Level;

/**
 * Command to delete an item from the collection by index
 */
@Log
public class RemoveAtCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public RemoveAtCommand(ICollectionManager<Organization> collectionManager) {
        super("remove_at","delete an item from the collection by index");
        addParameter("index", Integer.class);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started with {1}", new String[]{name, parameters.toString()});
        int index;
        try {
            index = (Integer) parameters.get("index");
        }
        catch (NullPointerException | ClassCastException e) {
            return new Response(ResponseStatus.ERROR, new NoSuchParameterRE(name, "index"));
        }

        try {
            if (userInfo.getRole().compareTo(Role.ADMIN) >= 0 ||
                    collectionManager.getAt(index).getUserInfo().getId() == userInfo.getId()) {
                Organization element = collectionManager.getAt(index);
                collectionManager.deleteAt(index);
                return new Response(ResponseStatus.DELETE, element);
            }
            else {
                return new Response(ResponseStatus.ERROR, new DeniedModificationRE());
            }
        }
        catch (IndexOutOfBoundsException e) {
            return new Response(ResponseStatus.ERROR, new RequestError() {
                @Override
                public String getMessage() {
                    return "index must be in bounds";
                }
            });
        }
    }
}
