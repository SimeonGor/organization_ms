package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.UserInfo;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Command to print all the elements of the collection in a string representation to the standard output stream
 */
@Log
public class ShowCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public ShowCommand(ICollectionManager<Organization> collectionManager) {
        super("show",
                "print all the elements of the collection in a string representation to the standard output stream");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(@NonNull UserInfo userInfo) {
        log.log(Level.INFO, "{0} command command started", name);
        return new Response(ResponseStatus.OK, new ArrayList<>(collectionManager.getAllItems()));
    }
}
