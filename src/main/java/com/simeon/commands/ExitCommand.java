package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Server;

/**
 * Class to exit the program (without saving to a file)
 */
public class ExitCommand extends Command {
    public ExitCommand(Server server) {
        super(server, "exit", "", false, "exit the program (without saving to a file)");
    }

    @Override
    public void execute(Client client) {
        System.exit(0);
    }
}
