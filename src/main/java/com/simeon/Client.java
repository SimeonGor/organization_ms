package com.simeon;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class for client that which interacts with the user
 * @see Server
 */
public class Client{
    private Server server;
    protected Scanner scanner;

    public Client(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    /**
     * connect to server
     * @param server server for connection
     */
    public void connect(Server server) {
        this.server = server;
    }

    private void setScanner(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }


    /**
     * Start main thread
     */
    public void start() {
        printResult("Print \"help\" to see available commands");
        while (true) {
            action(enterCommand());
        }
    }

    protected String enterCommand() {
        System.out.print("\u001B[34m>>> \u001B[0m");
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            printException("the program was interrupted");
            System.exit(0);
        }
        return null;
    }

    protected void action(String params) {
        server.executeRequest(new Request(this, server, params));
    }

    /**
     * the method of processing the server response
     * @param response instance of Response
     */
    public void receiveResponse(Response response) {
        if (response.isStatus()) {
            printResult(response.getMessage());
        }
        else {
            printException(response.getMessage());
        }
    }

    /**
     * method to get item parameters
     * @param parameters_name name of parameters
     * @return parameters in string form
     */
    public String getParameters(String parameters_name) {
        System.out.printf("\u001B[34mEnter %s: \u001B[0m", parameters_name);
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            printException("the program was interrupted");
            System.exit(0);
        }
        return null;
    }

    protected void printException(String message) {
        System.out.println(message);
    }

    protected void printResult(String result) {
        System.out.println(result);
    }
}
