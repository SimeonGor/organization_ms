package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.Serializable;
public class OrganizationTypeInputCommand implements InputCommand {

    @Override
    public Class<? extends Serializable> getInputType() {
        return OrganizationType.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        cli.printParameterPrompt(String.format("type { %s }",
                String.join(",", OrganizationType.listOfElements())));
        return OrganizationBuilder.getOrganizationType(cli.getScanner());
    }
}
