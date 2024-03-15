package com.simeon.commands;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.TreeSet;


/**
 * Command to execute the script from the specified file
 */
public class ExecuteScriptCommand extends Command {

    static private TreeSet<String> file_names = new TreeSet<>();
    public ExecuteScriptCommand(Server server) {
        super(server, "execute_script", "file_name", true, "execute the script from the specified file");
    }

    @Override
    public void execute(String params, Client client) {
        class SurrogateClient extends Client {
            public SurrogateClient(InputStream inputStream) {
                super(inputStream);
            }
            @Override
            public void receiveResponse(Response response) {
                client.receiveResponse(response);
            }
            @Override
            public void start() {
                while (scanner.hasNext()) {
                    action(enterCommand());
                }
                scanner.close();
            }

            @Override
            protected String enterCommand() {
                return scanner.nextLine();
            }


            @Override
            public String getParameters(String parameters_name) {
                return scanner.nextLine();
            }
        }

        String path = params;
        if (path.isBlank()) {
            client.receiveResponse(new Response(false, "print filename"));
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            client.receiveResponse(new Response(false, String.format("%s : no such file", path)));
            return;
        }
        if (!file.canRead()) {
            client.receiveResponse(new Response(false, String.format("%s : permission to read denied", path)));
            return;
        }

        try {
            Client surrogate = new SurrogateClient(new FileInputStream(file));
            surrogate.connect(server);
            if (!file_names.contains(path)) {
                file_names.add(path);
                surrogate.start();
                file_names.remove(path);
            }
            else {
                client.receiveResponse(new Response(false, "recursive call"));
            }
        } catch (FileNotFoundException e) {
            client.receiveResponse(new Response(false, String.format("%s : no such file", path)));
        }

    }
}
