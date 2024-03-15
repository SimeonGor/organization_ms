package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.collection.element.Address;
import com.simeon.collection.element.Coordinates;
import com.simeon.collection.element.Organization;
import com.simeon.collection.element.OrganizationType;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;


/**
 * Command to get new element
 * @see Organization
 */
public class NewElementBuilder {
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    public static String getName(Client client) {
        String name;
        while (true) {
            name = client.getParameters("name");
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "name", name);

            if (validates.isEmpty()) {
                break;
            }
            validates.forEach(e-> client.receiveResponse(new Response(false, e.getMessage())));
        }

        return name;
    }

    public static Coordinates getCoordinates(Client client) {
        Coordinates coordinates = new Coordinates();
        while (true) {
            try {
                int x = Integer.parseInt(client.getParameters("coordinates.x"));
                Set<ConstraintViolation<Coordinates>> validates =
                        validator.validateValue(Coordinates.class, "x", x);

                if (validates.isEmpty()) {
                    coordinates.setX(x);
                    break;
                }
                validates.forEach(e-> client.receiveResponse(new Response(false, e.getMessage())));
            } catch (NumberFormatException e) {
                client.receiveResponse(new Response(false, "it must be an integer"));
            }
        }
        while (true) {
            try {
                long y = Long.parseLong(client.getParameters("coordinates.y"));
                Set<ConstraintViolation<Coordinates>> validates =
                        validator.validateValue(Coordinates.class, "y", y);

                if (validates.isEmpty()) {
                    coordinates.setY(y);
                    break;
                }
                validates.forEach(e-> client.receiveResponse(new Response(false, e.getMessage())));
            } catch (NumberFormatException e) {
                client.receiveResponse(new Response(false, "it must be an integer"));
            }
        }
        return coordinates;
    }

    public static double getAnnualTurnover(Client client) {
        double annualTurnover;
        while (true) {
            try {
                annualTurnover = Double.parseDouble(client.getParameters("annual turnover"));
                Set<ConstraintViolation<Organization>> validates =
                        validator.validateValue(Organization.class, "annualTurnover", annualTurnover);

                if (validates.isEmpty()) {
                    break;
                }
                validates.forEach(e-> client.receiveResponse(new Response(false, e.getMessage())));
            } catch (NumberFormatException e) {
                client.receiveResponse(new Response(false, "it must be an number"));
            }
        }
        return annualTurnover;
    }

    public static OrganizationType getOrganizationType(Client client) {
        OrganizationType organizationType;
        while (true) {
             organizationType = OrganizationType.getByName(
                    client.getParameters("type" + OrganizationType.listOfElementsPrettyView()));

            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "type", organizationType);

            if (validates.isEmpty()) {
                break;
            }
            validates.forEach(e-> client.receiveResponse(new Response(false, e.getMessage())));
        }

        return organizationType;
    }

    public static Address getAddress(Client client) {
        Address address = new Address();
        while (true) {
            String t = client.getParameters("zip code");
            if (t.isEmpty()) {
                address.setZipCode(null);
            }
            else {
                address.setZipCode(t);
            }

            Set<ConstraintViolation<Address>> validates =
                    validator.validate(address);

            if (validates.isEmpty()) {
                break;
            }
            validates.forEach(e-> client.receiveResponse(new Response(false, e.getMessage())));
        }
        return address;
    }

    public static Organization getNewElement(Client client) {
        Organization element = new Organization();
        element.setName(getName(client));
        element.setCoordinates(getCoordinates(client));
        element.setAnnualTurnover(getAnnualTurnover(client));
        element.setType(getOrganizationType(client));
        element.setPostalAddress(getAddress(client));
        element.setCreationDate(LocalDate.now());
        return element;
    }
}
