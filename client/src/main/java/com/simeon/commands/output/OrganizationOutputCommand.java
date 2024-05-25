package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.Client;
import com.simeon.element.Organization;

import java.io.Serializable;

public class OrganizationOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return Organization.class;
    }

    @Override
    public void show(Serializable message, CLI cli) {
        String template = "%-4s| %-10s| %3s| %-6s| %-10s| %-15s| %-10s| %-9s | %-9s";
        String header = String.format("%-4s| %-10s| %-11s| %-10s| %-15s| %-10s| %-9s | %-9s",
                "id",
                "name",
                "coordinates",
                "date",
                "annual turnover",
                "type",
                "zip code",
                "user");
        Organization element = (Organization) message;
        String result = header + "\n" +
                String.format(template,
                        element.getId(),
                        element.getName(),
                        element.getCoordinates().getX(),
                        element.getCoordinates().getY(),
                        element.getCreationDate(),
                        element.getAnnualTurnover(),
                        element.getType(),
                        element.getPostalAddress(),
                        element.getUserInfo().getUsername());
        cli.print(result);
        cli.print("\n");
    }
}
