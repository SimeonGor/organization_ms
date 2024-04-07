package com.simeon.collection;

import com.simeon.element.Organization;
import com.simeon.exceptions.InvalidCollectionDataException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Class for managing the collection and its elements
 *
 * @see MyCollection
 */
//TODO log
public class CollectionManager implements ICollectionManager<Organization> {
    private MyCollection collection;
    private final String filepath;
    private final CollectionLoader collectionLoader;
    private final Sequence id_sequence;

    @Getter
    @Setter
    private Comparator<Organization> comparator;

    /**
     * Load collection in the file by collection loader
     * @param path filepath
     * @param collectionLoader instance of CollectionLoader
     * @throws InvalidCollectionDataException invalid collection data in the file
     * @throws IOException errors reading from a file
     */
    public CollectionManager(String path, CollectionLoader collectionLoader, Comparator<Organization> comparator) throws InvalidCollectionDataException, IOException {
        this.filepath = path;
        this.collectionLoader = collectionLoader;
        collection = collectionLoader.load(path);
        if (collection == null) {
            collection = new MyCollection();
            collection.setCreationDate(LocalDate.now());
        }
        this.comparator = comparator;
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
    @Override
    public String getInfo() {
        return "Type: " + "Organization" +
                "\nCreation date: " + collection.getCreationDate() +
                "\nModified date: " + collection.getModifiedDate() +
                "\nSize: " + collection.getCollection().size();
    }

    /**
     * add new item to the collection
     * @param e instance of Organization
     */
    @Override
    public void add(Organization e) {
        e.setId(id_sequence.nextValue());
        collection.add(e);
        collection.getCollection().sort(comparator);
    }

    /**
     * Save collection to the file
     * @throws IOException errors writing to a file
     */
    @Override
    public void save() throws IOException {
        collectionLoader.save(filepath, collection);
    }

    /**
     * clear the collection
     */
    @Override
    public void clear() {
        collection.getCollection().clear();
        collection.setModifiedDate(LocalDate.now());
    }

    /**
     * check if collection is empty
     * @return true if collection is empty
     */
    @Override
    public boolean isEmpty() {
        return collection.getCollection().isEmpty();
    }

    /**
     * get Stream
     * @return java.util.stream.Stream
     */
    @Override
    public Stream<Organization> getStream() {
        return collection.collection.stream();
    }

    /**
     * remove items in the collection in which the condition is true
     * @param predicate condition
     * @return true if the elements have been deleted
     */
    @Override
    public boolean removeWhere(Predicate<Organization> predicate) {
        return collection.getCollection().removeIf(predicate);
    }

    /**
     * remove item at index
     * @param index index in the collection
     * @return true if the elements have been deleted
     */
    @Override
    public boolean removeAt(int index) {
        try {
            collection.getCollection().remove(index);
            return true;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * update item in which the condition is true
     * @param predicate condition
     * @param item instance of Organization
     * @return true if the element have been upgraded
     */
    @Override
    public boolean updateWhere(Organization item, Predicate<Organization> predicate) {
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

    /**
     * get size of collection
     * @return size of collection
     */
    @Override
    public int size() {
        return collection.getCollection().size();
    }

    /**
     * Reorder the collection
     */
    @Override
    public void reorder() {
        Collections.reverse(collection.getCollection());
    }
}
