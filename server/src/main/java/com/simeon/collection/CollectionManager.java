package com.simeon.collection;

import com.simeon.CollectionInfo;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.element.Address;
import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.DBException;
import lombok.Setter;
import org.apache.commons.collections4.ListUtils;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.mockito.internal.matchers.Or;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class for managing the collection and its elements
 */
public class CollectionManager implements ICollectionManager<Organization> {
    private final Lock lock = new ReentrantLock();
    private List<Organization> collection;
    @Setter
    private Comparator<Organization> comparator;
    private final Connection connection;

    public CollectionManager(String host, String database, int port, String user, String password, Comparator<Organization> comparator) throws DBException {
        this.comparator = comparator;

        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DBException();
        }

        loadCollection();
    }

    private void loadCollection() {
        collection = new ArrayList<>();
        try {
            String query = "SELECT organizations.id as id, Organizations.name as name, \n" +
                    "       Organizations.x as x,\n" +
                    "       Organizations.y as y,\n" +
                    "       Organizations.annualTurnover as annualTurnover,\n" +
                    "       Organizations.creationDate as creationDate,\n" +
                    "       Organizations.postalAddress as postalAddress,\n" +
                    "       OT.type as type,\n" +
                    "       U.id as user_id,\n" +
                    "       U.username as username,\n" +
                    "       R.role as role\n" +
                    "FROM organizations\n" +
                    "JOIN OrganizationTypes OT on organizations.type_id = OT.id\n" +
                    "JOIN Users U on U.id = organizations.user_id\n" +
                    "JOIN Roles R on U.role_id = R.id;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                double annualTurnover = resultSet.getDouble("annualTurnover");
                LocalDate creationDate = resultSet.getDate("creationDate").toLocalDate();
                String postalAddress = resultSet.getString("postalAddress");
                String type = resultSet.getString("type");
                long user_id = resultSet.getLong("user_id");
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");

                Organization organization = Organization.builder()
                        .id(id)
                        .name(name)
                        .coordinates(new Coordinates(x, y))
                        .annualTurnover(annualTurnover)
                        .creationDate(creationDate)
                        .postalAddress(new Address(postalAddress))
                        .type(OrganizationType.getByName(type))
                        .userInfo(new UserInfo(user_id, username, Role.valueOf(role)))
                        .build();

                collection.add(organization);
            }
            collection.sort(comparator);
        } catch (SQLException ignored) {
            ;
        }
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
            Statement callId = connection.createStatement();
            ResultSet id = callId.executeQuery("SELECT nextval('organization_id_seq') as id;");
            id.next();
            long genId = id.getLong("id");

            String query = "INSERT INTO Organizations(id, name, x, y, creationDate, annualTurnover, type_id, postalAddress, user_id) VALUES\n" +
                    "(?, ?, ?, ?, current_date, ?, (select id from organizationtypes where type = ?), ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, genId);
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getCoordinates().getX());
            statement.setLong(4, entity.getCoordinates().getY());
            statement.setDouble(5, entity.getAnnualTurnover());
            statement.setString(6, entity.getType().toString());
            statement.setString(7, entity.getPostalAddress().getZipCode());
            statement.setLong(8, entity.getUserInfo().getId());

            statement.executeUpdate();
            entity.setId(genId);
            collection.add(entity);
            collection.sort(comparator);
            return entity;
        }
        catch (SQLException e) {
            return null;
        }
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

            String query = "UPDATE Organizations\n" +
                    "SET name = ?,\n" +
                    "    x = ?,\n" +
                    "    y = ?,\n" +
                    "    annualTurnover = ?,\n" +
                    "    postalAddress = ?,\n" +
                    "    type_id = ( SELECT id FROM OrganizationTypes WHERE type = ? )\n" +
                    "WHERE id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getCoordinates().getX());
            preparedStatement.setLong(3, entity.getCoordinates().getY());
            preparedStatement.setDouble(4, entity.getAnnualTurnover());
            preparedStatement.setString(5, entity.getType().toString());

            preparedStatement.executeUpdate();

            collection.remove(old);
            collection.add(entity);
            collection.sort(comparator);
            return entity;
        }
        catch (SQLException e) {
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void delete(Organization entity) {
        lock.lock();
        try {
            String query = "DELETE FROM Organizations WHERE id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();

            collection.remove(entity);
        } catch (SQLException e) {
            ;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteAll() {
        lock.lock();
        try {
            String query = "DELETE FROM Organizations;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            collection.clear();
        } catch (SQLException ignored) {
            ;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteAt(int index) throws IndexOutOfBoundsException {
        lock.lock();
        try {
            delete(collection.get(index));
        } finally {
            lock.unlock();
        }
    }
}
