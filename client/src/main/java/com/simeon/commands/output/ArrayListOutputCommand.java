package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Response;
import com.simeon.element.Organization;
import com.simeon.commands.UnknownCommandException;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ArrayListOutputCommand implements OutputCommand {
    protected OutputHandler outputHandler;
    @Override
    public Class<? extends Serializable> getOutputType() {
        return ArrayList.class;
    }

    @Override
    public void show(Response response, CLI cli) {
        int i = 1;
        ArrayList<Serializable> list = (ArrayList<Serializable>) response.getData();

        for (var e : list) {
            cli.print(String.format("#%d\t", i++));
            try {
                outputHandler.handle(new Response(response.getStatus(), e), cli);
            } catch (UnknownCommandException ex) {
                cli.print("unknown type\n");
            }
        }
    }
}
