package com.simeon.gui;

import com.simeon.Client;
import com.simeon.element.Organization;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class GUI extends JFrame {
    private static final ResourceBundle lang = ResourceBundle.getBundle("lang");
    private final Client client;
    private AuthDialog authDialog;
    private MapCanvas mapCanvas;
    private OrganizationInfo organizationInfo;
    private OrganizationFormDialog formDialog;

    private Table table;
    private JLabel userLabel;

    public GUI(Client client) {
        this.client = client;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @SneakyThrows
            @Override
            public void windowClosing(WindowEvent e) {
                client.send("exit", new HashMap<>());
            }
        });

        this.getContentPane().add(createGUI());
        pack();
        setVisible(true);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                client.send("show", new HashMap<>());
            }
        }, 0, 10000);
    }

    private JPanel createGUI() {
        authDialog = new AuthDialog(this, client);
        mapCanvas = new MapCanvas(this);
        organizationInfo = new OrganizationInfo();
        formDialog = new OrganizationFormDialog(client);
        table = new Table(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(Box.createHorizontalGlue());

        userLabel = new JLabel("NO_AUTH");

        JButton loginBtn = new JButton(lang.getString("log_in"));
        loginBtn.addActionListener(e -> authDialog.call());

        String[] languages = new String[]{"en", "es_SV", "is", "it", "ru"};
        JComboBox<String> langCombo = new JComboBox<String>(languages);
        langCombo.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                String l = (String) langCombo.getSelectedItem();
                Properties properties = new Properties();
                Locale.setDefault(new Locale(l));
                relocale();
                properties.setProperty("lang", l);
                properties.store(new FileOutputStream("setting.properties"), null);
            }
        });
        langCombo.setSelectedItem(Locale.getDefault().getLanguage());

        topPanel.add(userLabel);
        topPanel.add(loginBtn);
        topPanel.add(langCombo);

        panel.add(topPanel);

        JPanel tablePanel = new JPanel();
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        JButton addBtn = new JButton(lang.getString("add"));
        addBtn.setBackground(new Color(0xBAF3AF));
        addBtn.addActionListener(e -> formDialog.setMethod("add", null));

        JButton addIfMaxBtn = new JButton(lang.getString("addIfMax"));
        addIfMaxBtn.setBackground(new Color(0xBAF3AF));
        addIfMaxBtn.addActionListener(e -> formDialog.setMethod("add_if_max", null));

        JButton updateBtn = new JButton(lang.getString("update"));
        updateBtn.setBackground(new Color(0xFAD755));
        updateBtn.addActionListener(e -> formDialog.setMethod("update", organizationInfo.getOrganization()));

        JButton deleteBtn = new JButton(lang.getString("delete"));
        deleteBtn.setBackground(new Color(0xFF8B8B));
        deleteBtn.addActionListener(e -> {
            long id = organizationInfo.getOrganization().getId();
            HashMap<String, Long> params = new HashMap<>();
            params.put("id", id);
            client.send("delete_by_id", params);
        });

        JButton clearBtn = new JButton(lang.getString("clear"));
        clearBtn.setBackground(new Color(0xFF8B8B));
        clearBtn.addActionListener(e -> client.send("clear", new HashMap<>()));

        buttons.add(addBtn);
        buttons.add(addIfMaxBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);
        buttons.add(Box.createHorizontalGlue());

        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(table);
        tablePanel.add(buttons);

        panel.add(tablePanel);

        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.X_AXIS));
        view.add(organizationInfo);
        view.add(Box.createRigidArea(new Dimension(20, 20)));
        view.add(mapCanvas);

        panel.add(view);

        return panel;
    }

    public void select(long id) {
        Organization organization =table.getById(id);
        mapCanvas.select(organization);
        organizationInfo.show(organization);
    }

    public List<Organization> getCollection() {
        return table.getCollection();
    }

    public void add(Organization organization) {
        table.add(organization);
        mapCanvas.add(organization);
        if (organizationInfo.getOrganization() == null
                || organizationInfo.getOrganization().getId() == organization.getId()) {
            organizationInfo.show(organization);
        }
    }

    public void delete(Organization organization) {
        mapCanvas.delete(organization);
        table.delete(organization);
        organizationInfo.show(null);
    }

    public void errorAuth() {
        authDialog.errorAuth();
    }

    public void okAuth(String username) {
        userLabel.setText(username);
        authDialog.dispose();
    }

    private void relocale() {
        authDialog.relocale();
        formDialog.relocale();
        organizationInfo.relocale();
        organizationInfo.show(organizationInfo.getOrganization());
        table.relocale();
    }
}
