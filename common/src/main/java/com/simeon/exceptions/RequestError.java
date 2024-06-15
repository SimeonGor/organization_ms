package com.simeon.exceptions;


import java.io.Serializable;

/**
 * Класс, содержащий описание ошибки обработки запроса
 */
public interface RequestError extends Serializable {
    String getMessage();
}
