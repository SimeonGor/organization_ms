package com.simeon.commands.servercommands;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.Command;
import com.simeon.element.Organization;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.logging.Level;

/**
 * Command to save the collection to a file
 */
@Log
public class SaveCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public SaveCommand(ICollectionManager<Organization> collectionManager) {
        super("save", "save the collection to a file");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        log.log(Level.INFO, "save collection");
        try {
            collectionManager.save();
            return new Response(true, "Collection has been saved");
        }
        catch (AccessDeniedException e) {
            return new Response(false, "Check file's permissions");
        }
        catch (FileNotFoundException e) {
            return new Response(false, "File not found");
        }
        catch (IOException e) {
            return new Response(false, "Something went wrong (◞‸◟ㆀ)");
        }
    }
}
