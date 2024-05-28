package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.regex.Pattern;

public class OrganizationTypeInputCommand implements InputCommand {

    @Override
    public Class<? extends Serializable> getInputType() {
        return OrganizationType.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        cli.getScanner().skip(Pattern.compile(".*\n"));
        cli.printParameterPrompt(String.format("type { %s }",
                String.join(",", OrganizationType.listOfElements())));
        cli.block();
        return OrganizationBuilder.getOrganizationType(cli.getScanner());
    }
}
