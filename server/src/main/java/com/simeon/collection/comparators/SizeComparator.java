package com.simeon.collection.comparators;

import com.simeon.element.Organization;

import java.util.Comparator;

import java.lang.instrument.Instrumentation;

/**
 * Util class
 */
class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}

/**
 * Compare by object size
 */
public class SizeComparator implements Comparator<Organization> {
    @Override
    public int compare(Organization o1, Organization o2) {
        return Long.compare(ObjectSizeFetcher.getObjectSize(o1), ObjectSizeFetcher.getObjectSize(o2));
    }
}
