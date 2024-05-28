package com.simeon.commands.input;

import com.simeon.CLI;

public class InputHandlerFactory {
    public static InputHandler getInputHandler() {
        InputHandler inputHandler = new InputHandler();
        inputHandler.add(new IntegerInputCommand());
        inputHandler.add(new StringInputCommand());
        inputHandler.add(new OrganizationInputCommand());
        inputHandler.add(new OrganizationTypeInputCommand());
        inputHandler.add(new LongInputCommand());
        return inputHandler;
    }
}
