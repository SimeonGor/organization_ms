package com.simeon.element.comparator;

import com.simeon.element.Organization;

import java.util.Comparator;

public class IDComparator implements Comparator<Organization> {
    @Override
    public int compare(Organization o1, Organization o2) {
        return Long.compare(o1.getId(), o2.getId());
    }
}
