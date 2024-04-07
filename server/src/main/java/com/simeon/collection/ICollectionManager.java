package com.simeon.collection;

import java.io.IOException;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ICollectionManager<T> {
     String getInfo();
     void add(T element);
     void save() throws IOException;
     void clear();
     boolean isEmpty();
     Stream<T> getStream();
     boolean removeWhere(Predicate<T> predicate);
     boolean removeAt(int index);
     boolean updateWhere(T element, Predicate<T> predicate);
     int size();
     void reorder();
     Comparator<T> getComparator();
}
