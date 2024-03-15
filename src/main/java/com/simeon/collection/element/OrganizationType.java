package com.simeon.collection.element;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Enum for organization type
 */
public enum OrganizationType {
    COMMERCIAL {
        @Override
        public String toString() {
            return "commercial";
        }
    },
    PUBLIC {
        @Override
        public String toString() {
            return "public";
        }
    },
    PRIVATE_LIMITED_COMPANY {
        @Override
        public String toString() {
            return "PLC";
        }
    },
    OPEN_JOINT_STOCK_COMPANY {
        @Override
        public String toString() {
            return "OJSC";
        }
    };

    /**
     * Return array of the types of organization
     * @return String[]
     */
    public static String[] listOfElements() {
        ArrayList<String> result = new ArrayList<>();
        for (var e : values()) {
            result.add(e.toString());
        }

        return result.toArray(new String[0]);
    }

    /**
     * Return a string listing the types of organization in the form { ... }
     * @return String
     */
    public static String listOfElementsPrettyView() {
        StringBuilder result = new StringBuilder();
        result.append("{ ");
        for (var e : listOfElements()) {
            result.append(e).append(", ");
        }
        result.delete(result.length() - 2, result.length()).append(" }");
        return result.toString();
    }

    /**
     * Return an instance of the organization type
     * @param s name of the organization type
     * @return OrganizationType
     */
    public static OrganizationType getByName(@NotNull String s) {
        for (var e : values()) {
            if (s.equals(e.toString())) {
                return e;
            }
        }
        return null;
    }
}