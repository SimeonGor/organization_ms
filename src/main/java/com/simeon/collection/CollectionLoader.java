package com.simeon.collection;

import com.simeon.exceptions.InvalidCollectionDataException;

import java.io.IOException;

/**
 * Collection loader interface
 * @see CollectionManager
 */
public interface CollectionLoader {
    MyCollection load(String path) throws IOException, InvalidCollectionDataException;
    void save(String path, MyCollection collection) throws IOException;
}
