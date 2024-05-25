package com.simeon.commands;

import com.simeon.CommandInfo;
import com.simeon.Request;
import com.simeon.Response;
import com.simeon.Token;
import com.simeon.connection.ConnectionChannel;
import com.simeon.exceptions.UnknownCommandException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerCommandHandler extends CommandHandler {
    private HashMap<String, CommandInfo> commands = new HashMap<>();
    private ConnectionChannel connectionChannel;

    public ServerCommandHandler(ConnectionChannel connectionChannel) {
        this.connectionChannel = connectionChannel;
    }

    public void setCommands(ArrayList<CommandInfo> commands) {
        for (var command : commands) {
            this.commands.put(command.getName(), command);
        }
    }

    @Override
    public ArrayList<CommandInfo> getCommands() {
        return new ArrayList<>(commands.values());
    }

    @Override
    public CommandInfo getCommandInfoOf(String method) throws UnknownCommandException {
        if (!commands.containsKey(method)) {
            throw new UnknownCommandException(method);
        }
        return commands.get(method);
    }

    @Override
    public Response handle(String method, HashMap<String, ? extends Serializable> parameters, Token token) throws UnknownCommandException {
        if (!commands.containsKey(method)) {
            throw new UnknownCommandException(method);
        }

        connectionChannel.send(new Request(token, method, parameters));
        Response response = connectionChannel.receive();
        if (response == null) {
            return new Response(false, "an error in data transmission or the server crashed");
        }
        return response;
    }
}
