package com.simeon.commands;

import com.simeon.Response;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.exceptions.UnknownCommandException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    @Getter
    @Setter
    protected CommandHandler successor;
    @Getter
    protected final Role level;

    public CommandHandler(Role role) {
        this.successor = null;
        this.level = role;
    }

    protected Map<String, ICommand> commands = new HashMap<>();

    public boolean addCommand(ICommand command) {
        if (commands.containsKey(command.getName())) {
            return false;
        }
        commands.put(command.getName(), command);
        return true;
    }

    public Map<String, ICommand> getCommands() {
        Map<String, ICommand> result = new HashMap<>();
        result.putAll(commands);
        result.putAll(successor.getCommands());
        return result;
    }

    public Response handle(String method, Map<String, ? extends Serializable> parameters, UserInfo userInfo) {
        if (userInfo.getRole().compareTo(level) < 0) {
            return new Response(false, new UnknownCommandException(method));
        }

        if (commands.containsKey(method)) {
            ICommand command = commands.get(method);
            if (!parameters.isEmpty()) {
                return command.execute(parameters, userInfo);
            }
            else {
                return command.execute(userInfo);
            }
        }
        else {
            if (successor != null) {
                return successor.handle(method, parameters, userInfo);
            }
            else {
                return new Response(false, new UnknownCommandException(method));
            }
        }
    }
}
