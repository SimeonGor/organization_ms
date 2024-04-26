package com.simeon.commands.servercommands;

import com.simeon.Response;
import com.simeon.Server;
import com.simeon.collection.CollectionManager;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.logging.Level;

@Log
public class ExitCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    protected final Server server;
    public ExitCommand(ICollectionManager<Organization> collectionManager, Server server) {
        super("exit", "exit the program");
        this.collectionManager = collectionManager;
        this.server = server;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "exit program");
        try {
            collectionManager.save();
            server.close();
            return new Response(true, "collection was saved");
        } catch (IOException e) {
            return new Response(false, "the collection could not be saved");
        }
    }
}
