package com.simeon.element.comparator;

import com.simeon.element.Organization;

import java.util.Comparator;

public class AnnualTurnoverComparator implements Comparator<Organization> {
    @Override
    public int compare(Organization o1, Organization o2) {
        if (o1.getAnnualTurnover().compareTo(o2.getAnnualTurnover()) == 0) {
            return o1.getName().compareTo(o2.getName());
        }
        else {
            return (-1) * o1.getAnnualTurnover().compareTo(o2.getAnnualTurnover());
        }
    }
}
