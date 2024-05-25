package com.simeon.commands;

import com.simeon.CommandInfo;
import com.simeon.Response;
import com.simeon.Token;
import com.simeon.exceptions.UnknownCommandException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class CommandHandler {
    @Getter
    @Setter
    protected CommandHandler successor;

    public abstract ArrayList<CommandInfo> getCommands();
    public abstract CommandInfo getCommandInfoOf(String method) throws UnknownCommandException;
    public abstract Response handle(String method, HashMap<String, ? extends Serializable> parameters, Token token) throws UnknownCommandException;
}
