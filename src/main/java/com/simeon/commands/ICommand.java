package com.simeon.commands;

import com.simeon.Client;
import com.simeon.exceptions.InvalidCommandParametersException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Interface for command pattern.
 */
public interface ICommand {
    String getName();
    String getParameters();
    String getDescription();

    boolean hasParameters();

    /**
     * Execute the command with parameters
     * @param parameters
     * @param client
     */
    void execute(@NotBlank String parameters, @NotNull Client client) throws InvalidCommandParametersException;

    /**
     * Execute the command without parameters
     * @param client
     */
    void execute(@NotNull Client client) throws InvalidCommandParametersException;
}
