package com.simeon.commands.servercommands;

import com.simeon.Response;
import com.simeon.Server;
import com.simeon.UserInfo;
import com.simeon.commands.Command;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public class ExitCommand extends Command {
    protected final Server server;
    public ExitCommand(Server server) {
        super("exit", "exit the program");
        this.server = server;
    }

    @Override
    public Response execute(@NonNull UserInfo userInfo) {
        server.close();
        return new Response(true, "collection was saved");
    }
}
