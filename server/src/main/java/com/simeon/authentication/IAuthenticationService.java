package com.simeon.authentication;

import com.simeon.Role;
import com.simeon.Token;
import com.simeon.User;
import com.simeon.UserInfo;
import com.simeon.exceptions.UnauthorizedUserException;

public interface IAuthenticationService {
    Token authentication(String login, String password) throws UnauthorizedUserException;
    Token register(String username, String password);
    UserInfo verified(Token token) throws UnauthorizedUserException;
    UserInfo getUserById(long id);
}
