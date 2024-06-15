package com.simeon.commands;

import com.simeon.CLI;
import com.simeon.Client;
import com.simeon.Response;
import com.simeon.ResponseStatus;
import com.simeon.exceptions.InvalidArgumentException;
import com.simeon.exceptions.RequestError;
import lombok.NonNull;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class ExecuteScriptCommand extends Command {
    public static Set<String> filenames = new TreeSet<>();
    private final Client client;
    public ExecuteScriptCommand(Client client) {
        super("execute_script", "execute the script from the specified file");
        addParameter("filename", String.class);
        this.client = client;
    }

    @Override
    public Response execute(@NonNull HashMap<String, ? extends Serializable> parameters) {
        if (!parameters.containsKey("filename")) {
            return new Response(ResponseStatus.ERROR, new RequestError() {
                @Override
                public String getMessage() {
                    return "filename is required parameter";
                }
            });
        }
        String filename = (String) parameters.get("filename");
        try {
            File file = new File(filename);
            if (!file.canRead()) {
                return new Response(ResponseStatus.ERROR, String.format("%s : permission to read denied", filename));
            }

            if (!filenames.contains(filename)) {
                CLI old_cli = client.getCli();
                CLI replacing_cli = new CLI(new FileInputStream(file),
                                            old_cli.getOutputStream(), old_cli.getErrorStream());
                replacing_cli.setGui(old_cli.getGui());
                replacing_cli.setInteractiveMode(false);
                client.setCli(replacing_cli);

                filenames.add(filename);
                client.loop();
                filenames.remove(filename);
                client.setCli(old_cli);
            }
            else {
                return new Response(ResponseStatus.ERROR, new RequestError() {
                    @Override
                    public String getMessage() {
                        return "recursive call";
                    }
                });
            }
        } catch (ClassCastException e) {
            return new Response(ResponseStatus.ERROR, new RequestError() {
                @Override
                public String getMessage() {
                    return "The filename must be string";
                }
            });
        } catch (FileNotFoundException e) {
            return new Response(ResponseStatus.ERROR, new RequestError() {
                @Override
                public String getMessage() {
                    return String.format("%s : no such file", filename);
                }
            });
        }

        return new Response(ResponseStatus.OK, "The script has been executed successfully");
    }
}
