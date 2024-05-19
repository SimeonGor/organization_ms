package com.simeon.commands;

import com.simeon.Response;
import lombok.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for command pattern.
 */
public interface ICommand {
    String getName();
    Map<String, Class<? extends Serializable>> getParameterTypes();
    String getDescription();

    boolean hasParameters();

    /**
     * Execute the command with parameters
     * @param parameters map of parameters
     */
    Response execute(@NonNull Map<String, ? extends Serializable> parameters);

    /**
     * Execute the command without parameters
     */
    Response execute();
}
