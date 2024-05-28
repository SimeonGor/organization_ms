package com.simeon.element;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Enum for organization type
 */
public enum OrganizationType implements Serializable {
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

    @Serial
    private static final long serialVersionUID = 0L;

    /**
     * Return array of the types of organization
     * @return List
     */
    public static List<String> listOfElements() {
        ArrayList<String> result = new ArrayList<>();
        for (var e : values()) {
            result.add(e.toString());
        }

        return result;
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