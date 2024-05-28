package com.simeon.commands.input;

import com.simeon.CLI;
import com.simeon.exceptions.InvalidArgumentException;

import java.io.*;

public interface InputCommand {
    Class<? extends Serializable> getInputType();

    /**
     * Получить предложение к вводу
     * @return предложение к вводу. null если не требуется
     */
//    String getSuggest();

    Serializable read(CLI cli) throws InvalidArgumentException;
}
