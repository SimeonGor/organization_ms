package com.simeon;

import com.simeon.collection.CollectionManager;
import com.simeon.commands.Command;
import com.simeon.commands.ICommand;
import com.simeon.exceptions.InvalidCommandParametersException;
import com.simeon.exceptions.UnknownCommandException;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;

/**
 * Class to server
 * @see Client
 */
public class Server {
    private final HashMap<String, ICommand> commands = new HashMap<>();
    @Getter
    private final CollectionManager collectionManager;

    public Server(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * add new Command
     * @param command
     * @see Command
     */
    public void setCommand(ICommand command) {
        this.commands.put(command.getName(), command);
    }

    /**
     * Get collection of commands
     * @return collections of commands
     */
    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    /**
     * process the request
     * @param request instance of Request
     * @see Request
     * @see Client
     */
    public void executeRequest(Request request) {
        try {
            executeCommand(request.getCommandName(), request.getParameters(), request.getClient());
        } catch (UnknownCommandException e) {
            request.getClient().receiveResponse(new Response(false,
                    e.getMessage() + "\nPrint \"help\" to see available commands"));
        } catch (InvalidCommandParametersException e) {
            request.getClient().receiveResponse(
                    new Response(false, e.getMessage()));
        }
    }

    /**
     * Execute command by name
     * @param commandName command name
     * @param parameters string with parameters
     * @param client
     * @throws UnknownCommandException
     */
    private void executeCommand(String commandName, String parameters, Client client) throws UnknownCommandException, InvalidCommandParametersException {
        if (commands.containsKey(commandName)) {
            var command = commands.get(commandName);
            if (command.hasParameters() && !parameters.isBlank()) {
                command.execute(parameters, client);
            }
            else if (!command.hasParameters() && parameters.isBlank()) {
                command.execute(client);
            }
            else {
                throw new InvalidCommandParametersException();
            }
        }
        else {
            throw new UnknownCommandException(commandName);
        }
    }
}
