package com.simeon.authentication;

import com.simeon.Token;
import com.simeon.UserInfo;
import com.simeon.exceptions.AuthorizedException;
import com.simeon.exceptions.BusyUsernameException;

public interface IAuthenticationService {
    Token authentication(String login, String password) throws AuthorizedException;
    Token register(String username, String password) throws BusyUsernameException;
    UserInfo verified(Token token) throws AuthorizedException;
    UserInfo getUserById(long id);
}
