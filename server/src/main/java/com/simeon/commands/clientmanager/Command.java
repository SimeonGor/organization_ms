package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.commands.ICommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Command abstract class
 */
@Log
public abstract class Command<T> implements ICommand {
    @Getter
    protected String name, description;
    protected final ICollectionManager<T> collectionManager;
    @Getter(AccessLevel.PUBLIC)
    private HashMap<String, Class<?>> parameterTypes;
    protected boolean hasParameters;

    public Command(ICollectionManager<T> collectionManager, String name, String description) {
        this.collectionManager = collectionManager;
        this.name = name;
        this.description = description;
        this.hasParameters = false;
    }

    protected void addParameter(String name, Class<?> type) {
        parameterTypes.put(name, type);
        hasParameters = true;
    }

    /**
     * Return true if command has parameters.
     * @return
     */
    @Override
    public boolean hasParameters() {
        return hasParameters;
    }

    /**
     * Execution stub with the parameter. Throws exception if it doesn't have parameters.
     * @param parameters map of parameters
     */
    @Override
    public Response execute(@NonNull HashMap<String, Object> parameters) {
        log.log(Level.FINE, "Execution stub with the parameter started");
        if (!hasParameters()) {
            return new Response(false, "Invalid parameters");
        }
        return new Response(false, null);
    }

    /**
     * Execution stub without the parameter. Throws exception if it has parameters.
     */
    @Override
    public Response execute() {
        log.log(Level.FINE, "Execution stub without the parameter started");
        if (hasParameters()) {
            return new Response(false, "Invalid parameters");
        }
        return new Response(false, null);
    }
}
