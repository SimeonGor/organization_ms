package com.simeon.authentication;

import com.google.common.hash.Hashing;
import com.simeon.Role;
import com.simeon.Token;
import com.simeon.User;
import com.simeon.UserInfo;
import com.simeon.exceptions.UnauthorizedUserException;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuthenticationService implements IAuthenticationService {
    private final Lock lock = new ReentrantLock();
    private Connection connection;

    private Map<Long, User> users;

    public AuthenticationService() {

        loadUsers();
    }

    private void loadUsers() {
        users = new HashMap<>();
    }

    @Override
    public Token authentication(String login, String password) throws UnauthorizedUserException {
        lock.lock();
        try {
            List<User> result = users.values().stream()
                    .filter(user -> user.getUsername().equals(login))
                    .toList();
            if (result.isEmpty()) {
                throw new UnauthorizedUserException();
            }
            User user = result.get(0);

            String hashPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            if (hashPassword.equals(user.getPassword())) {
                return new Token(user.getId(), user.getUsername(), password);
            }
            else {
                throw new UnauthorizedUserException();
            }
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Token register(String username, String password) {
        lock.lock();
        try {
            Long genId = 10L;

            users.put(genId, new User(genId, username, password, Role.USER));
            return new Token(genId, username, password);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public UserInfo verified(@NonNull Token token) {
        lock.lock();
        try {
            String hashPassword = Hashing.sha256().hashString(token.getPassword(), StandardCharsets.UTF_8).toString();
            User user = users.get(token.getId());
            if (hashPassword.equals(user.getPassword())) {
                return getUserById(token.getId());
            }
            else {
                return new UserInfo(token.getId(), token.getUsername(), Role.NO_AUTH);
            }
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
