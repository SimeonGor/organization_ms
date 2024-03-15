package com.simeon.collection;


import com.simeon.collection.comarators.AnnualTurnoverComparator;
import com.simeon.collection.element.Organization;
import com.simeon.exceptions.InvalidCollectionDataException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Class for managing the collection and its elements
 *
 * @see MyCollection
 */
public class CollectionManager {
    private MyCollection collection;
    private final String filepath;
    private final CollectionLoader collectionLoader;
    private final Sequence id_sequence;

    @Getter
    @Setter
    private Comparator<Organization> comparator = new AnnualTurnoverComparator();

    /**
     * Load collection in the file by collection loader
     * @param path filepath
     * @param collectionLoader instance of CollectionLoader
     * @throws InvalidCollectionDataException invalid collection data in the file
     * @throws IOException errors reading from a file
     */
    public CollectionManager(String path, CollectionLoader collectionLoader) throws InvalidCollectionDataException, IOException {
        this.filepath = path;
        this.collectionLoader = collectionLoader;
        collection = collectionLoader.load(path);
        if (collection == null) {
            collection = new MyCollection();
            collection.setCreationDate(LocalDate.now());
        }
        collection.getCollection().sort(comparator);

        long maxId = 0;
        for (var e : collection.getCollection()) {
            if (e.getId() > maxId) {
                maxId = e.getId();
            }
        }

        id_sequence = new Sequence(maxId, 1, Long.MAX_VALUE);
    }

    /**
     * get info about the collection
     * @return string with info
     */
    public String getInfo() {
        StringBuilder result = new StringBuilder();
        result.append("Type: ").append("Organization")
                .append("\nCreation date: ").append(collection.getCreationDate())
                .append("\nModified date: ").append(collection.getModifiedDate())
                .append("\nSize: ").append(collection.getCollection().size());
        return result.toString();
    }

    /**
     * add new item to the collection
     * @param e instance of Organization
     */
    public void add(Organization e) {
        e.setId(id_sequence.nextValue());
        collection.add(e);
        collection.getCollection().sort(comparator);
    }

    /**
     * Save collection to the file
     * @throws IOException errors writing to a file
     */
    public void save() throws IOException {
        collectionLoader.save(filepath, collection);
    }

    /**
     * clear the collection
     */
    public void clear() {
        collection.getCollection().clear();
        collection.setModifiedDate(LocalDate.now());
    }

    /**
     * check if collection is empty
     * @return true if collection is empty
     */
    public boolean isEmpty() {
        return collection.getCollection().isEmpty();
    }

    /**
     * get a collection in a string view
     * @return collection in a string view
     */
    public String getCollectionAsString() {
        return collection.toString();
    }

    /**
     * get a collection in a string view in which the condition is true
     * @param predicate condition
     * @return new collection with items in which the condition is true
     */
    public String selectWhere(Predicate<Organization> predicate) {
        MyCollection result = new MyCollection();
        for (var e : collection.getCollection()) {
            if (predicate.test(e)) {
                result.add(e);
            }
        }
        result.getCollection().sort(comparator);
        return result.toString();
    }

    /**
     * remove items in the collection in which the condition is true
     * @param predicate condition
     * @return true if the elements have been deleted
     */
    public boolean removeWhere(Predicate<Organization> predicate) {
        return collection.getCollection().removeIf(predicate);
    }

    /**
     * remove item at index
     * @param index index in the collection
     */
    public void removeAt(int index) {
        collection.getCollection().remove(index);
    }

    /**
     * find minimum item
     * @param comp comparator
     * @return instance of Organization
     */
    public Organization minIf(Comparator<Organization> comp) {
        return Collections.min(collection.getCollection(), comp);
    }
    /**
     * find maximum item
     * @param comp comparator
     * @return instance of Organization
     */
    public Organization maxIf(Comparator<Organization> comp) {
        return Collections.max(collection.getCollection(), comp);
    }

    /**
     * update item in which the condition is true
     * @param predicate condition
     * @param item instance of Organization
     * @return true if the elements have been replaced
     */
    public boolean updateWhere(Predicate<Organization> predicate, Organization item) {
        boolean isReplaced = false;
        for (int i = 0; i < collection.getCollection().size(); i++) {
            Organization element = collection.getCollection().get(i);
            if (predicate.test(element)) {
                item.setId(element.getId());
                collection.getCollection().removeElementAt(i);
                collection.getCollection().add(i, item);
                isReplaced = true;
            }
        }
        collection.getCollection().sort(comparator);

        return isReplaced;
    }

    public int size() {
        return collection.getCollection().size();
    }

    public void reorder() {
        Collections.reverse(collection.getCollection());
    }
}
