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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class OrganizationBuilder {
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    public static String getName(Scanner scanner)  throws InvalidArgumentException {
        String name = null;
        try {
            name = scanner.nextLine();
        }
        catch (NoSuchElementException ignored) {
        }
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
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
            int x = nf.parse(scanner.nextLine()).intValue();
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "coordinates.x", x);

            if (validates.isEmpty()) {
                return x;
            }
            throw new InvalidArgumentException(
                    String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
            );
        } catch (ParseException | NoSuchElementException | NumberFormatException e) {
            throw new InvalidArgumentException("Coordinates.x must be an integer");
        }
    }

    public static long getCoordinatesY(Scanner scanner) throws InvalidArgumentException {
        try {
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
            long y = nf.parse(scanner.nextLine()).longValue();
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "coordinates.y", y);

            if (validates.isEmpty()) {
                return y;
            }
            throw new InvalidArgumentException(
                    String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
            );
        } catch (ParseException | InputMismatchException | NumberFormatException e) {
            throw new InvalidArgumentException("Coordinates.y must be an integer");
        }
    }

    public static double getAnnualTurnover(Scanner scanner) throws InvalidArgumentException {
        try {
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
            double annualTurnover = nf.parse(scanner.nextLine()).doubleValue();
            Set<ConstraintViolation<Organization>> validates =
                    validator.validateValue(Organization.class, "annualTurnover", annualTurnover);

            if (validates.isEmpty()) {
                return annualTurnover;
            }
            throw new InvalidArgumentException(
                    String.join("\n", validates.stream().map(ConstraintViolation::getMessage).toList())
            );
        } catch (ParseException| NoSuchElementException | NumberFormatException e) {
            throw new InvalidArgumentException("Annual turnover must be an floating point number");
        }
    }

    public static OrganizationType getOrganizationType(Scanner scanner) throws InvalidArgumentException {
        OrganizationType organizationType = null;
        try {
            organizationType =  OrganizationType.getByName(scanner.nextLine());
        } catch (NoSuchElementException ignored) {

        }
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
        Address address;
        String t = scanner.nextLine();
        if (t.isEmpty()) {
            address = new Address(null);
        }
        else {
            address = new Address(t);
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
