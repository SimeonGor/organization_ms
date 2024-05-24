package com.simeon.collection;

import com.simeon.CollectionInfo;
import com.simeon.element.Organization;
import lombok.Setter;
import org.apache.commons.collections4.ListUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class for managing the collection and its elements
 */
//TODO log
public class CollectionManager implements ICollectionManager<Organization> {
    private final Lock lock = new ReentrantLock();
    private List<Organization> collection;
    @Setter
    private Comparator<Organization> comparator;
    private final Connection connection;

    public CollectionManager(String host, String database, int port, String user, String password, Comparator<Organization> comparator) {
        this.comparator = comparator;

        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loadCollection();
    }

    private void loadCollection() {
        // TODO: 23.05.2024 загрузка из бд
        collection = new ArrayList<>();
    }

    @Override
    public List<Organization> getAllItems() {
        lock.lock();
        try {
            return List.copyOf(collection);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Organization getById(long id) {
        lock.lock();
        try {
            List<Organization> results = ListUtils.select(collection, e -> e.getId() == id);
            if (results.isEmpty()) {
                throw new NoSuchElementException();
            }
            return results.get(0);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Organization getAt(int index) {
        lock.lock();
        try {
            return collection.get(index);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public CollectionInfo getInfo() {
        return new CollectionInfo(collection.size());
    }

    @Override
    public Organization create(Organization entity) {
        lock.lock();
        try {
            // TODO: 23.05.2024 создание в бд

            collection.add(entity);
            collection.sort(comparator);
            return entity;
        }
//        catch (SQLException e) {
//            return null;
//        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Organization createIfMax(Organization entity) {
        lock.lock();
        try {
            Organization maxElement = collection.get(collection.size() - 1);
            if (comparator.compare(entity, maxElement) < 0) {
                return null;
            }

            return create(entity);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Organization update(Organization entity) {
        lock.lock();
        try {
            Organization old = getById(entity.getId());

            // TODO: 23.05.2024 update в бд

            collection.remove(old);
            collection.add(entity);
            collection.sort(comparator);
            return entity;
        }
//        catch (SQLException e) {
//            return null;
//        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void delete(Organization entity) {
        lock.lock();
        try {
            // TODO: 23.05.2024 удаление в бд

            collection.remove(entity);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteAll() {
        lock.lock();
        try {
            // TODO: 23.05.2024 удаление в бд

            collection.clear();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteAt(int index) throws IndexOutOfBoundsException {
        lock.lock();
        try {
            // TODO: 23.05.2024 удаление в бд

            collection.remove(index);
        } finally {
            lock.unlock();
        }
    }
}
