package com.simeon.gui;

import com.simeon.Client;
import com.simeon.element.Organization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ResourceBundle;

public class OrganizationFormDialog extends JDialog {
    private static ResourceBundle lang = ResourceBundle.getBundle("lang");

    private final JButton actionButton;
    private final JButton cancelBtn;
    private final OrganizationUpdate organizationUpdate;
    public OrganizationFormDialog(Client client) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container main = getContentPane();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);

        organizationUpdate = new OrganizationUpdate();

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setOpaque(false);

        actionButton = new JButton(lang.getString("save"));
        actionButton.addActionListener(new SaveAction(organizationUpdate, client));

        cancelBtn = new JButton(lang.getString("cancel"));
        cancelBtn.setBackground(new Color(0xFF8B8B));
        cancelBtn.addActionListener(e -> dispose());

        buttons.add(actionButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(cancelBtn);

        main.add(organizationUpdate);
        main.add(buttons);
        pack();

        setAlwaysOnTop(true);
    }

    public void setMethod(String method, Organization organization) {
        organizationUpdate.show(organization);

        actionButton.setActionCommand(method);
        setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private class SaveAction implements ActionListener {
        private final OrganizationUpdate organizationUpdate;
        private final Client client;
        public SaveAction(OrganizationUpdate organizationUpdate, Client client) {
            this.organizationUpdate = organizationUpdate;
            this.client = client;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String method = e.getActionCommand();
            Organization element = organizationUpdate.getOrganization();
            pack();
            if (element != null) {
                HashMap<String, Serializable> parameters = new HashMap<>();
                parameters.put("id", element.getId());
                parameters.put("element", element);
//                System.out.println(element);
                client.send(method, parameters);
                dispose();
            }
            else {
                System.out.println("error");
            }
        }
    }

    public void relocale() {
        lang = ResourceBundle.getBundle("lang");
        actionButton.setText(lang.getString("save"));
        cancelBtn.setText(lang.getString("cancel"));
        organizationUpdate.relocale();
    }
}