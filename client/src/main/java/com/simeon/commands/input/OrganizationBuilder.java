package com.simeon.commands.input;

import com.simeon.element.Address;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.InvalidArgumentException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class OrganizationBuilder {
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    public static String getName(Scanner scanner)  throws InvalidArgumentException {
        String name = scanner.nextLine();
        Set<ConstraintViolation<Organization>> validates =
                validator.validateValue(Organization.class, "name", name);

        if (validates.isEmpty()) {
            return name;
        }
        throw new InvalidArgumentException(
                String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
        );
    }

    public static int getCoordinatesX(Scanner scanner) throws InvalidArgumentException {
        try {
            int x = Integer.parseInt(scanner.nextLine());
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "coordinates.x", x);

            if (validates.isEmpty()) {
                return x;
            }
            throw new InvalidArgumentException(
                    String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
            );
        } catch (InputMismatchException | NumberFormatException e) {
            throw new InvalidArgumentException("it must be an integer");
        }
    }

    public static long getCoordinatesY(Scanner scanner) throws InvalidArgumentException {
        try {
            long y = Long.parseLong(scanner.nextLine());
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "coordinates.y", y);

            if (validates.isEmpty()) {
                return y;
            }
            throw new InvalidArgumentException(
                    String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
            );
        } catch (InputMismatchException | NumberFormatException e) {
            throw new InvalidArgumentException("it must be an integer");
        }
    }

    public static double getAnnualTurnover(Scanner scanner) throws InvalidArgumentException {
        try {
            double annualTurnover = Double.parseDouble(scanner.nextLine());
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "annualTurnover", annualTurnover);

            if (validates.isEmpty()) {
                return annualTurnover;
            }
            throw new InvalidArgumentException(
                    String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
            );
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("it must be an floating point number");
        }
    }

    public static OrganizationType getOrganizationType(Scanner scanner) throws InvalidArgumentException {
        OrganizationType organizationType = OrganizationType.getByName(scanner.nextLine());
        Set<ConstraintViolation<Organization>> validates =
                validator.validateValue(Organization.class, "type", organizationType);

        if (validates.isEmpty()) {
            return organizationType;
        }
        throw new InvalidArgumentException(
                String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
        );
    }

    public static Address getAddress(Scanner scanner) throws InvalidArgumentException {
        Address address = new Address();
        String t = scanner.nextLine();
        if (t.isEmpty()) {
            address.setZipCode(null);
        }
        else {
            address.setZipCode(t);
        }

        Set<ConstraintViolation<Address>> validates =
                validator.validate(address);

        if (validates.isEmpty()) {
            return address;
        }

        throw new InvalidArgumentException(
                String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
        );
    }
}
