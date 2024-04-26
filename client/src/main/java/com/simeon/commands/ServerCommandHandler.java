package com.simeon.commands;

import com.simeon.CommandInfo;
import com.simeon.Request;
import com.simeon.Response;
import com.simeon.connection.IReceiver;
import com.simeon.connection.ISender;
import com.simeon.exceptions.UnknownCommandException;
import lombok.AccessLevel;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerCommandHandler extends CommandHandler {
    protected HashMap<String, CommandInfo> commands = new HashMap<>();
    protected ISender sender;
    protected IReceiver receiver;

    public ServerCommandHandler(ISender sender, IReceiver receiver) {
        this.sender = sender;
        this.receiver = receiver;
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
    public Response handle(String method, HashMap<String, ? extends Serializable> parameters) throws UnknownCommandException {
        if (!commands.containsKey(method)) {
            throw new UnknownCommandException(method);
        }

        sender.send(new Request(method, parameters));
        Response response = receiver.receive();
        if (response == null) {
            return new Response(false, "an error in data transmission or the server crashed");
        }
        return response;
    }
}
