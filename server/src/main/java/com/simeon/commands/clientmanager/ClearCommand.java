package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Command to clearing the collection
 */
@Log
public class ClearCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public ClearCommand(ICollectionManager<Organization> collectionManager) {
        super("clear", "clear the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(@NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started", name);

        List<Organization> list = collectionManager.getAllItems().parallelStream()
                .filter((e) -> userInfo.getRole().compareTo(Role.ADMIN) >= 0 || e.getUserInfo().getId() == userInfo.getId())
                .toList();

        collectionManager.getAllItems().parallelStream()
                .filter((e) -> userInfo.getRole().compareTo(Role.ADMIN) >= 0 || e.getUserInfo().getId() == userInfo.getId())
                .forEach(collectionManager::delete);

        return new Response(ResponseStatus.DELETE, new ArrayList<>(list));
    }
}
