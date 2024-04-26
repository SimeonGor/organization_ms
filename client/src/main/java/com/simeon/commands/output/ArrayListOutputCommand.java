package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.exceptions.UnknownCommandException;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@AllArgsConstructor
public class ArrayListOutputCommand implements OutputCommand {
    protected OutputHandler outputHandler;
    @Override
    public Class<? extends Serializable> getOutputType() {
        return ArrayList.class;
    }

    @Override
    public void show(Serializable message, CLI cli) {
        int i = 1;
        for (var e : (ArrayList<Serializable>) message) {
            cli.print(String.format("#%d\t", i++));
            try {
                outputHandler.handle(e, cli);
            } catch (UnknownCommandException ex) {
                cli.print("unknown type\n");
            }
        }
    }
}
