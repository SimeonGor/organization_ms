package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.UserInfo;
import com.simeon.authentication.IAuthenticationService;
import com.simeon.commands.Command;
import com.simeon.exceptions.AuthorizedException;
import com.simeon.exceptions.NoSuchParameterException;
import com.simeon.exceptions.UnauthorizedUserException;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Map;

public class LoginCommand extends Command {
    private final IAuthenticationService authenticationService;
    public LoginCommand(IAuthenticationService authenticationService) {
        super("login", "log in to your account");
        addParameter("username", String.class);
        addParameter("password", String.class);
        this.authenticationService = authenticationService;
    }

    @Override
    public Response execute(@NonNull Map<String, ? extends Serializable> parameters, @NonNull UserInfo userInfo) {
        String username;
        try {
            username = (String) parameters.get("username");
        }
        catch (ClassCastException e) {
            return new Response(false, e);
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "username"));
        }

        String password;
        try {
            password = (String) parameters.get("password");
        }
        catch (ClassCastException e) {
            return new Response(false, e);
        }
        catch (NullPointerException e) {
            return new Response(false, new NoSuchParameterException(name, "password"));
        }

        try {
            return new Response(true, authenticationService.authentication(username, password));
        } catch (AuthorizedException e) {
            return new Response(false, e);
        }
    }
}
