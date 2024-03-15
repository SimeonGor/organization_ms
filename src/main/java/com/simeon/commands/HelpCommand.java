package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;

/**
 * Command to print this help message to the output stream
 */
public class HelpCommand extends Command {
    public HelpCommand(Server server) {
        super(server, "help", "", false,
                "print this help message to the output stream");
    }

    @Override
    public void execute(Client client) {
        StringBuilder t = new StringBuilder();
        for (var i : server.getCommands()) {
            t.append(String.format("\t\u001B[1m%-40s\u001B[0m \u001B[3m%-15s\u001B[0m %s\n",
                    i.getName(), i.getParameters(), i.getDescription()));
        }

        client.receiveResponse(new Response(true, t.substring(0, t.length() - 1)));
    }
}
