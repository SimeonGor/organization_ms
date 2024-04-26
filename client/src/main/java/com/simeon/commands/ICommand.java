package com.simeon.commands;

import com.simeon.Response;
import com.simeon.exceptions.InvalidArgumentException;
import lombok.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Interface for command pattern.
 */
public interface ICommand {
     String getName();
     HashMap<String, Class<? extends Serializable>> getParameterTypes();
     String getDescription();

     boolean hasParameters();

     /**
      * Execute the command with parameters
      * @param parameters map of parameters
      */
     Response execute(@NonNull HashMap<String, ? extends Serializable> parameters) throws InvalidArgumentException;

     /**
      * Execute the command without parameters
      */
     Response execute() throws InvalidArgumentException;
}
