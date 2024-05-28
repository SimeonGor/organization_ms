package com.simeon.commands;

import com.simeon.Response;
import com.simeon.exceptions.InvalidArgumentException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;

/**
 * Command abstract class
 */
@Log
public abstract class Command implements ICommand {
    @Getter
    protected String name, description;
    @Getter(AccessLevel.PUBLIC)
    private LinkedHashMap<String, Class<? extends Serializable>> parameterTypes = new LinkedHashMap<>();
    protected boolean hasParameters;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        this.hasParameters = false;
    }

    protected void addParameter(String name, Class<? extends Serializable> type) {
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
    public Response execute(@NonNull HashMap<String, ? extends Serializable> parameters) throws InvalidArgumentException {
        log.log(Level.FINE, "Execution stub with the parameter started");
        if (!hasParameters()) {
            throw new InvalidArgumentException("This method doesn't have parameters");
        }
        return new Response(false, null);
    }

    /**
     * Execution stub without the parameter. Throws exception if it has parameters.
     */
    @Override
    public Response execute() throws InvalidArgumentException {
        log.log(Level.FINE, "Execution stub without the parameter started");
        if (hasParameters()) {
            throw new InvalidArgumentException("This method  have parameters");
        }
        return new Response(false, null);
    }
}
