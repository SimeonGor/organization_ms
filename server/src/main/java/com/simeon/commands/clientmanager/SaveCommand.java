package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * Command to save the collection to a file
 */
public class SaveCommand extends Command {
    protected final ICollectionManager<Organization> collectionManager;
    public SaveCommand(ICollectionManager<Organization> collectionManager) {
        super("save", "save the collection to a file");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
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
