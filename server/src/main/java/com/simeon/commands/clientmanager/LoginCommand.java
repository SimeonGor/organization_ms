package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.UserInfo;
import com.simeon.authentication.AuthorizedException;
import com.simeon.authentication.IAuthenticationService;
import com.simeon.commands.Command;
import com.simeon.exceptions.AuthorizationRE;
import com.simeon.exceptions.NoSuchParameterRE;
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
        catch (NullPointerException | ClassCastException e) {
            return new Response(ResponseStatus.ERROR, new NoSuchParameterRE(name, "username"));
        }

        String password;
        try {
            password = (String) parameters.get("password");
        }
        catch (NullPointerException | ClassCastException e) {
            return new Response(ResponseStatus.ERROR, new NoSuchParameterRE(name, "password"));
        }

        try {
            return new Response(ResponseStatus.OK, authenticationService.authentication(username, password));
        } catch (AuthorizedException e) {
            return new Response(ResponseStatus.ERROR, new AuthorizationRE("wrong username or password"));
        }
    }
}
