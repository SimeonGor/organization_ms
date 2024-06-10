package com.simeon.element.comparator;

import com.simeon.element.Organization;

import java.time.LocalDate;
import java.util.Comparator;

public class CreationDateComparator implements Comparator<Organization> {
    @Override
    public int compare(Organization o1, Organization o2) {
       return o1.getCreationDate().compareTo(o2.getCreationDate());
    }
}
