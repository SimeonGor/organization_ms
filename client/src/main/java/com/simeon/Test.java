package com.simeon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Test {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Client.class.getResourceAsStream("/setting.properties"));
        System.out.println(properties.getProperty("lang"));
        Locale.setDefault(new Locale(properties.getProperty("lang")));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("lang");
        System.out.println(Locale.getDefault().getDisplayName());
        System.out.println(resourceBundle.getString("log_in"));
    }
}
