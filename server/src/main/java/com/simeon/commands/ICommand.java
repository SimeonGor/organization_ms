package com.simeon.commands;

import com.simeon.Response;
import com.simeon.exceptions.InvalidCommandParametersException;
import lombok.NonNull;

import java.util.HashMap;

/**
 * Interface for command pattern.
 */
public interface ICommand {
    String getName();
    HashMap<String, Class<?>> getParameterTypes();
    String getDescription();

    boolean hasParameters();

    /**
     * Execute the command with parameters
     * @param parameters map of parameters
     */
    Response execute(@NonNull HashMap<String, Object> parameters);

    /**
     * Execute the command without parameters
     */
    Response execute();
}
