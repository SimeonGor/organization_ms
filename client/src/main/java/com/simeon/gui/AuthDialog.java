package com.simeon.gui;

import com.simeon.Client;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;

@Log
public class AuthDialog extends JDialog {
    public class AuthButtonsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            log.log(Level.INFO, command);

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            client.send(command, params);
        }
    }


    private static ResourceBundle lang = ResourceBundle.getBundle("lang");
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel header;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    private final Client client;
    public AuthDialog(JFrame parent, Client client) {
        super(parent, lang.getString("authorization"));
        this.client = client;

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(createGUI());
        pack();
        setResizable(false);
        setAlwaysOnTop(true);
    }

    private JPanel createGUI() {
        header = new JLabel(lang.getString("authorization"));
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(2, 2));
        usernameLabel = new JLabel(lang.getString("username"), SwingConstants.RIGHT);
        passwordLabel = new JLabel(lang.getString("password"), SwingConstants.RIGHT);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        form.add(usernameLabel);
        form.add(usernameField);
        form.add(passwordLabel);
        form.add(passwordField);
        form.setOpaque(false);

        loginButton = new JButton(lang.getString("log_in"));
        registerButton = new JButton(lang.getString("register"));
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(loginButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(registerButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder (BorderFactory.createEmptyBorder(12,12,12,12));
        panel.add(header, CENTER_ALIGNMENT);
        panel.add(form);
        panel.add(buttons);
        panel.setBackground(new Color(0xFFFDEF));

        loginButton.setActionCommand("login");
        registerButton.setActionCommand("register");

        loginButton.addActionListener(new AuthButtonsListener());
        registerButton.addActionListener(new AuthButtonsListener());

        return panel;
    }

    public void errorAuth() {
        usernameField.setBackground(new Color(0xFFA1A1));
        passwordField.setBackground(new Color(0xFFA1A1));
    }

    public void call() {
        usernameField.setBackground(Color.WHITE);
        passwordField.setBackground(Color.WHITE);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void relocale() {
        lang = ResourceBundle.getBundle("lang");
        header.setText(lang.getString("authorization"));
        usernameLabel.setText(lang.getString("username"));
        passwordLabel.setText(lang.getString("password"));
        loginButton.setText(lang.getString("log_in"));
        registerButton.setText(lang.getString("register"));

        header.repaint();
        usernameLabel.repaint();
        passwordLabel.repaint();
        loginButton.repaint();
        registerButton.repaint();
        repaint();
    }
}
