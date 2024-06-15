package com.simeon.authentication;

import com.simeon.Token;
import com.simeon.UserInfo;

public interface IAuthenticationService {
    Token authentication(String login, String password) throws AuthorizedException;
    Token register(String username, String password) throws BusyUsernameException;
    UserInfo verified(Token token) throws AuthorizedException;
}
