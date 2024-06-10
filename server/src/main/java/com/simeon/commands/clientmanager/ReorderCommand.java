package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Command to reorder the collection
 */
@Log
public class ReorderCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public ReorderCommand(ICollectionManager<Organization> collectionManager) {
        super("reorder", "reorder the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(@NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started", name);
        List<Organization> result = new ArrayList<>(collectionManager.getAllItems());
        Collections.reverse(result);
        return new Response(true,
                new ArrayList<>(result));
    }
}
