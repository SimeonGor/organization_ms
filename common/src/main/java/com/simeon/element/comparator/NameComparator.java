package com.simeon.element.comparator;

import com.simeon.element.Organization;

import java.util.Comparator;

public class NameComparator implements Comparator<Organization> {
    @Override
    public int compare(Organization o1, Organization o2) {
        return o1.getName().compareTo(o2.getName());
    }
}

