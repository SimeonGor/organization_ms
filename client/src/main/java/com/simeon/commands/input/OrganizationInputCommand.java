package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.InvalidArgumentException;
import java.io.Serializable;
import java.util.regex.Pattern;

public class OrganizationInputCommand implements InputCommand {
    @Override
    public Class<? extends Serializable> getInputType() {
        return Organization.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        cli.getScanner().skip(Pattern.compile(".*\n"));

        Organization.OrganizationBuilder element = Organization.builder();
        while (true) {
            try {
                cli.printParameterPrompt("name");
                element.name(OrganizationBuilder.getName(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        Coordinates.CoordinatesBuilder coordinates = Coordinates.builder();
        while (true) {
            try {
                cli.printParameterPrompt("coordinates.x");
                coordinates.x(OrganizationBuilder.getCoordinatesX(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        while (true) {
            try {
                cli.printParameterPrompt("coordinates.y");
                coordinates.y(OrganizationBuilder.getCoordinatesY(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        element.coordinates(coordinates.build());

        while (true) {
            try {
                cli.printParameterPrompt("annual turnover");
                element.annualTurnover(OrganizationBuilder.getAnnualTurnover(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }

        while (true) {
            try {
                cli.printParameterPrompt(String.format("type { %s }",
                        String.join(",", OrganizationType.listOfElements())));
                element.type(OrganizationBuilder.getOrganizationType(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }

        while (true) {
            try {
                cli.printParameterPrompt("address");
                element.postalAddress(OrganizationBuilder.getAddress(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        cli.block();
        return element.build();
    }
}
