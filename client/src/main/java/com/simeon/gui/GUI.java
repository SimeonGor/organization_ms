package com.simeon.gui;

import com.simeon.Client;
import com.simeon.Response;
import com.simeon.ResponseHandler;
import com.simeon.element.Organization;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GUI extends JFrame {
    private static final ResourceBundle lang = ResourceBundle.getBundle("lang");
    private final Client client;
    private AuthDialog authDialog;
    private MapCanvas mapCanvas;
    private OrganizationInfo organizationInfo;
    private OrganizationFormDialog formDialog;

    private Table table;

    public GUI(Client client) {
        this.client = client;

        this.getContentPane().add(createGUI());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JPanel createGUI() {
        authDialog = new AuthDialog(this, client);
        mapCanvas = new MapCanvas(this);
        organizationInfo = new OrganizationInfo();
        formDialog = new OrganizationFormDialog(client);
        table = new Table();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel tablePanel = new JPanel();
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        JButton addBtn = new JButton(lang.getString("add"));
        addBtn.setBackground(new Color(0xBAF3AF));
        addBtn.addActionListener(e -> formDialog.setMethod("add", null));

        JButton addIfMaxBtn = new JButton(lang.getString("addIfMax"));
        addIfMaxBtn.setBackground(new Color(0xBAF3AF));
        addIfMaxBtn.addActionListener(e -> formDialog.setMethod("addIfMax", null));

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
        organizationInfo.show(table.getById(id));
    }

    public void errorAuth() {
        authDialog.errorAuth();
    }

    public void okAuth() {
        authDialog.dispose();
    }

    public static void main(String[] args) {
        JFrame jFrame = new GUI(null);

    }
}
