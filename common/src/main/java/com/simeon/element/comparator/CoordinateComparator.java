package com.simeon.element.comparator;

import com.simeon.element.Organization;

import java.util.Comparator;

public class CoordinateComparator implements Comparator<Organization> {
    @Override
    public int compare(Organization o1, Organization o2) {
        if (o1.getCoordinates().getX() == o2.getCoordinates().getX()) {
            return Long.compare(o1.getCoordinates().getY(), o2.getCoordinates().getY());
        }
        else {
            return (-1) * o1.getAnnualTurnover().compareTo(o2.getAnnualTurnover());
        }
    }
}
