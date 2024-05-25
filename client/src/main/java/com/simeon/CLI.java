package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
    protected boolean interactiveMode = true;
    private boolean block = false;

    public CLI(InputStream inputStream, PrintStream outputStream, PrintStream errorStream) {
        this.inputStream = inputStream;
        this.scanner = new Scanner(inputStream);
        this.outputStream = outputStream;
        this.errorStream = errorStream;
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
}
