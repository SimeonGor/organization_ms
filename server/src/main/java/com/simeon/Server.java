package com.simeon;

import com.simeon.collection.CollectionManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Class to server
 */
@Log
@AllArgsConstructor
public class Server {
    @Getter
    private final CollectionManager collectionManager;
}
