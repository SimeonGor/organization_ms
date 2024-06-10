package com.simeon.authentication;

import com.google.common.hash.Hashing;
import com.simeon.Role;
import com.simeon.Token;
import com.simeon.User;
import com.simeon.UserInfo;
import com.simeon.exceptions.AuthorizedException;
import com.simeon.exceptions.BusyUsernameException;
import com.simeon.exceptions.DBException;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuthenticationService implements IAuthenticationService {
    private final Lock lock = new ReentrantLock();
    private final Connection connection;
    private Map<Long, User> users;

    public AuthenticationService(String host, String database, int port, String user, String password) throws DBException {
        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DBException();
        }

        loadUsers();
    }

    private String getHashPassword(String password, String salt) {
        return Hashing.sha256().hashString(salt + password, StandardCharsets.UTF_8).toString();
    }

    private String getSalt() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    private void loadUsers() {
        users = new HashMap<>();
        try {
            String query = "SELECT users.id as id, users.username as username, users.password as password, users.salt as salt, roles.role as role\n" +
                    "    FROM users INNER JOIN Roles ON users.role_id = roles.id;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String salt = resultSet.getString("salt");

                User user = new User(id, username, password, salt, Role.valueOf(role));
                users.put(id, user);
            }
        } catch (SQLException ignored) {
            ;
        }
    }

    @Override
    public Token authentication(String login, String password) throws AuthorizedException {
        lock.lock();
        try {
            List<User> result = users.values().stream()
                    .filter(user -> user.getUsername().equals(login))
                    .toList();
            if (result.isEmpty()) {
                throw new AuthorizedException();
            }
            User user = result.get(0);

            String hashPassword = getHashPassword(password, user.getSalt());

            if (hashPassword.equals(user.getPassword())) {
                return new Token(user.getId(), user.getUsername(), password);
            }
            else {
                throw new AuthorizedException();
            }
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Token register(String username, String password) throws BusyUsernameException {
        lock.lock();
        try {
            Statement callId = connection.createStatement();
            ResultSet rs = callId.executeQuery("SELECT nextval('user_id_seq') as id;");
            rs.next();
            long genId = rs.getLong("id");
            String salt = getSalt();
            String hashPassword = getHashPassword(password, salt);
            String query = "INSERT INTO Users(id, username, password, salt, role_id)\n" +
                    "VALUES (?, ?, ?, ?, 3);";
            PreparedStatement addStatement = connection.prepareStatement(query);
            addStatement.setLong(1, genId);
            addStatement.setString(2, username);
            addStatement.setString(3, hashPassword);
            addStatement.setString(4, salt);
            int count = addStatement.executeUpdate();
            users.put(genId, new User(genId, username, hashPassword, salt, Role.USER));
            return new Token(genId, username, password);
        } catch (SQLException e) {
            throw new BusyUsernameException(username);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserInfo verified(Token token) {
        lock.lock();
        try {
            User user = users.get(token.getId());
            String hashPassword = getHashPassword(token.getPassword(), user.getSalt());
            if (hashPassword.equals(user.getPassword())) {
                return new UserInfo(user.getId(), user.getUsername(), user.getRole());
            }
            else {
                return new UserInfo(token.getId(), token.getUsername(), Role.NO_AUTH);
            }
        }
        catch (NullPointerException e) {
            return new UserInfo(-1, "unauthorized", Role.NO_AUTH);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public UserInfo getUserById(long id) {
        lock.lock();
        try {
            if (id < 0) {
                try {
                    User user = users.get(id);
                    return new UserInfo(id, user.getUsername(), user.getRole());
                } catch (NoSuchElementException e) {
                    return null;
                }
            } else {
                return new UserInfo(id, "unauthorized", Role.NO_AUTH);
            }
        }
        finally {
            lock.unlock();
        }
    }
}
