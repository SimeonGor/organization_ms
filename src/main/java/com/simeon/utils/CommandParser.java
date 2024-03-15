package com.simeon.utils;

/**
 * Class to parse the string containing the query
 */
public class CommandParser {
    private final String commandName;
    private final String parameters;

    public CommandParser(String string) {
        String[] t = string.split(" ", 2);
        if (t.length > 0) {
            commandName = t[0];
        }
        else {
            commandName = "";
        }

        if (t.length > 1) {
            parameters = t[1];
        }
        else {
            parameters = "";
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String getParameters() {
        return parameters;
    }
}
