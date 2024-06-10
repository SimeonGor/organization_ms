package com.simeon.commands;

import com.simeon.Response;
import com.simeon.UserInfo;
import lombok.NonNull;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interface for command pattern.
 */
public interface ICommand {
    String getName();
    LinkedHashMap<String, Class<? extends Serializable>> getParameterTypes();
    String getDescription();

    boolean hasParameters();

    /**
     * Execute the command with parameters
     * @param parameters map of parameters
     */
    Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo);

    /**
     * Execute the command without parameters
     */
    Response execute(@NonNull UserInfo userInfo);
}
