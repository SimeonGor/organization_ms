package com.simeon;

import com.simeon.authentication.IAuthenticationService;
import com.simeon.commands.CommandHandler;
import com.simeon.connection.ConnectionChannel;
import com.simeon.exceptions.AuthorizedException;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class RequestHandler {
    private final CommandHandler commandHandler;
    private final ResponseHandler responseHandler;
    private final IAuthenticationService authenticationService;

    public RequestHandler(CommandHandler commandHandler, ResponseHandler responseHandler, IAuthenticationService authenticationService) {
        this.commandHandler = commandHandler;
        this.responseHandler = responseHandler;
        this.authenticationService = authenticationService;
    }

    public void handleRequest(Request request, ConnectionChannel connectionChannel) {
        Thread thread = new Thread(new Handler(request, connectionChannel));
        thread.start();
    }

    public void close() {
        responseHandler.close();
    }


    private class Handler implements Runnable {
        private final Request request;
        private final ConnectionChannel connectionChannel;
        public Handler(Request request, ConnectionChannel connectionChannel) {
            this.request = request;
            this.connectionChannel = connectionChannel;
        }
        @Override
        public void run() {
            if (request != null) {
                log.log(Level.INFO, () -> "process " + request.getMethod());
                Response response;
                try {
                    UserInfo userInfo = authenticationService.verified(request.getUserToken());
                    log.log(Level.INFO, () -> userInfo.getRole().toString());
                    response = commandHandler.handle(request.getMethod(), request.getParams(), userInfo);
                } catch (AuthorizedException e) {
                    response = new Response(false, e);
                }
                responseHandler.send(response, connectionChannel);
            }
        }
    }
}
