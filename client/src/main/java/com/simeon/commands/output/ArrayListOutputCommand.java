package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.element.Organization;
import com.simeon.exceptions.UnknownCommandException;
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
    public void show(Serializable message, CLI cli) {
        int i = 1;
        ArrayList<Serializable> list = (ArrayList<Serializable>) message;
        if (list.get(0).getClass().equals(Organization.class)) {
            ArrayList<Organization> orgList = (ArrayList<Organization>) message;
            List<Organization> old = cli.getCollection();
            for (var e : orgList) {
                old.removeIf((o) -> o.getId() == e.getId());
            }
            for (var e : old) {
                cli.delete(e);
            }
        }

        for (var e : list) {
            cli.print(String.format("#%d\t", i++));
            try {
                outputHandler.handle(e, cli);
            } catch (UnknownCommandException ex) {
                cli.print("unknown type\n");
            }
        }
    }
}
