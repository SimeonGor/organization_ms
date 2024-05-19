package com.simeon.collection;

import com.simeon.CollectionInfo;

import java.util.List;

public interface ICollectionManager<T> {
     T create(final T entity);
     T createIfMax(final T entity);
     void delete(final T entity);
     void deleteAll();
     void deleteAt(int index) throws IndexOutOfBoundsException;
     void deleteById(final long entityId);
     List<T> getAllItems();
     CollectionInfo getInfo();
     T update(final T entity);
}
