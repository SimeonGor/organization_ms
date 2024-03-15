package com.simeon;

import com.simeon.collection.CollectionManager;
import com.simeon.collection.JsonCollectionLoader;
import com.simeon.commands.*;
import com.simeon.exceptions.InvalidCollectionDataException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            CollectionManager collectionManager = new CollectionManager(args[0], new JsonCollectionLoader());
            Server server = new Server(collectionManager);
            server.setCommand(new HelpCommand(server));
            server.setCommand(new ExitCommand(server));
            server.setCommand(new ShowCommand(server));
            server.setCommand(new SaveCommand(server));
            server.setCommand(new ClearCommand(server));
            server.setCommand(new RemoveByIDCommand(server));
            server.setCommand(new RemoveAtCommand(server));
            server.setCommand(new InfoCommand(server));
            server.setCommand(new ReorderCommand(server));
            server.setCommand(new MinByPostalAddressCommand(server));
            server.setCommand(new FilterGreaterThanTypeCommand(server));
            server.setCommand(new PrintFieldDescendingPostalAddressCommand(server));
            server.setCommand(new AddCommand(server));
            server.setCommand(new UpdateCommand(server));
            server.setCommand(new AddIfMaxCommand(server));
            server.setCommand(new ExecuteScriptCommand(server));

            Client client = new Client(System.in);
            client.connect(server);
            client.start();
        } catch (IOException | InvalidCollectionDataException e) {
            System.out.println(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No arguments");
        } finally {
            ;
        }
    }
}