package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.InvalidArgumentException;
import java.io.Serializable;

public class OrganizationInputCommand implements InputCommand {
    @Override
    public Class<? extends Serializable> getInputType() {
        return Organization.class;
    }

    @Override
    public Serializable read(CLI cli) throws InvalidArgumentException {
        Organization element = new Organization();
        while (true) {
            try {
                cli.printParameterPrompt("name");
                element.setName(OrganizationBuilder.getName(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        Coordinates coordinates = new Coordinates();
        while (true) {
            try {
                cli.printParameterPrompt("coordinates.x");
                coordinates.setX(OrganizationBuilder.getCoordinatesX(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        while (true) {
            try {
                cli.printParameterPrompt("coordinates.y");
                coordinates.setY(OrganizationBuilder.getCoordinatesY(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }
        element.setCoordinates(coordinates);

        while (true) {
            try {
                cli.printParameterPrompt("annual turnover");
                element.setAnnualTurnover(OrganizationBuilder.getAnnualTurnover(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }

        while (true) {
            try {
                cli.printParameterPrompt(String.format("type { %s }",
                        String.join(",", OrganizationType.listOfElements())));
                element.setType(OrganizationBuilder.getOrganizationType(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }

        while (true) {
            try {
                cli.printParameterPrompt("address");
                element.setPostalAddress(OrganizationBuilder.getAddress(cli.getScanner()));
                break;
            } catch (InvalidArgumentException e) {
                cli.error(e);
            }
        }

        return element;
    }
}
