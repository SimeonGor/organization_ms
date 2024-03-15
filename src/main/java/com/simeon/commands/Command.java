package com.simeon.commands;


import com.simeon.Client;
import com.simeon.Server;
import com.simeon.exceptions.InvalidCommandParametersException;

/**
 * Command abstract class
 * @see ICommand
 */
public abstract class Command implements ICommand {
    protected String name, parameters, description;
    protected final Server server;
    protected final boolean hasParameters;

    public Command(Server server, String name, String parameters, Boolean hasParameters, String description) {
        this.server = server;
        this.name = name;
        this.parameters = parameters;
        this.hasParameters = hasParameters;
        this.description = description;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getParameters() {
        return parameters;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean hasParameters() {
        return hasParameters;
    }

    @Override
    public void execute(String parameters, Client client) throws InvalidCommandParametersException {
        if (!hasParameters()) {
            throw new InvalidCommandParametersException();
        }
    }

    @Override
    public void execute(Client client) throws InvalidCommandParametersException {
        if (hasParameters()) {
            throw new InvalidCommandParametersException();
        }
    }
}
