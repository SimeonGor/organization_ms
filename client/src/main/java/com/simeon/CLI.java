package com.simeon;

import com.simeon.element.Organization;
import com.simeon.exceptions.RequestError;
import com.simeon.gui.GUI;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Setter
@Getter
@Log
public class CLI {
    protected InputStream inputStream;
    @Getter
    protected Scanner scanner;
    protected PrintStream outputStream;
    protected PrintStream errorStream;
    @Setter
    private GUI gui;
    protected boolean interactiveMode = true;
    private boolean block = false;

    public CLI(InputStream inputStream, PrintStream outputStream, PrintStream errorStream) {
        this.inputStream = inputStream;
        this.scanner = new Scanner(inputStream);
        this.outputStream = outputStream;
        this.errorStream = errorStream;
    }

    public void add(Organization organization) {
        if (gui != null) {
            gui.add(organization);
        }
    }

    public void delete(Organization organization) {
        if (gui != null) {
            gui.delete(organization);
        }
    }

    public List<Organization> getCollection() {
        return gui.getCollection();
    }
    public void okAuth(String username) {
        if (gui != null) {
            gui.okAuth(username);
        }
    }

    public void errorAuth() {
        if (gui != null) {
            gui.errorAuth();
        }
    }

    public void block() {
        block = true;
    }
    @SneakyThrows
    public void flush() {
        if (block) {
            block = false;
            return;
        }
        if (interactiveMode) {
            scanner.skip(Pattern.compile(".*"));
        } else {
            if (scanner.hasNext()) {
                scanner.skip(Pattern.compile(".*\n"));
            }
        }
    }

    @SneakyThrows
    public void close() {
        scanner.close();
        inputStream.close();
        outputStream.close();
        errorStream.close();
    }

    public void print(String message) {
        outputStream.print(message);
    }

    public void printShellPrompt() {
        if (interactiveMode) {
            outputStream.print("\u001B[34m>>> \u001B[0m");
        }
    }

    public void error(Exception exception) {
        // можно менять реализацию
        log.log(Level.WARNING, exception.getMessage(), exception);
        errorStream.println(exception.getMessage());
    }

    public void printParameterPrompt(String parameter_name) {
        if (interactiveMode) {
            outputStream.printf("\u001B[34mEnter %s: \u001B[0m", parameter_name);
        }
    }

    public void printError(String message) {
        outputStream.println(message);
        gui.showPopup(message);
    }
}
