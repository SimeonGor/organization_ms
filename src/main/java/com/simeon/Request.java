package com.simeon;

import com.simeon.utils.CommandParser;
import lombok.Getter;

/**
 * Class for client's request
 */
@Getter
public class Request {
    private final Client client;
    private final Server server;
    private final String command, parameters;

    public Request(Client client, Server server, String request) {
        this.client = client;
        this.server = server;
        CommandParser commandParser = new CommandParser(request);
        this.command = commandParser.getCommandName();
        this.parameters = commandParser.getParameters();
    }

    public String getCommandName() {
        return command;
    }

}
