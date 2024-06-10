package com.simeon.gui;

import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.commands.input.OrganizationBuilder;
import com.simeon.element.Address;
import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.exceptions.InvalidArgumentException;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class OrganizationUpdate extends JPanel {
    private static final ResourceBundle lang = ResourceBundle.getBundle("lang");
    private final JTextField nameField;
    private final JLabel nameErrMsg;
    private final JComboBox<String> typeField;
    private final JTextField annualTurnoverField;
    private final JLabel annualTurnoverErrMsg;
    private final JTextField postalAddressField;
    private final JLabel postalAddressErrMsg;
    private final JTextField coordinateXField;
    private final JLabel coordinateXErrMsg;
    private final JTextField coordinateYField;
    private final JLabel coordinateYErrMsg;

    private Organization organization;
    public OrganizationUpdate() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        nameField = new JTextField();
        nameField.setFont(new Font("Helvetica", Font.BOLD, 16));
        nameErrMsg = new JLabel();
        nameErrMsg.setFont(new Font("Helvetica", Font.ITALIC, 11));
        namePanel.add(nameField);
        namePanel.add(nameErrMsg);


        String[] items = OrganizationType.listOfElements().toArray(new String[0]);
        typeField = new JComboBox<>(items);

        JPanel annualTurnoverPanel = new JPanel();
        annualTurnoverPanel.setLayout(new BoxLayout(annualTurnoverPanel, BoxLayout.Y_AXIS));
        annualTurnoverPanel.setOpaque(false);
        annualTurnoverField = new JTextField();
        annualTurnoverField.setPreferredSize(new Dimension(50, 20));
        annualTurnoverErrMsg = new JLabel();
        annualTurnoverErrMsg.setFont(new Font("Helvetica", Font.ITALIC, 11));
        annualTurnoverPanel.add(annualTurnoverField);
        annualTurnoverPanel.add(annualTurnoverErrMsg);

        JPanel postalAddressPanel = new JPanel();
        postalAddressPanel.setLayout(new BoxLayout(postalAddressPanel, BoxLayout.Y_AXIS));
        postalAddressPanel.setOpaque(false);
        postalAddressField = new JTextField();
        postalAddressField.setPreferredSize(new Dimension(50, 20));
        postalAddressErrMsg = new JLabel();
        postalAddressErrMsg.setFont(new Font("Helvetica", Font.ITALIC, 11));
        postalAddressPanel.add(postalAddressField);
        postalAddressPanel.add(postalAddressErrMsg);

        JPanel coordinateXPanel = new JPanel();
        coordinateXPanel.setLayout(new BoxLayout(coordinateXPanel, BoxLayout.Y_AXIS));
        coordinateXPanel.setOpaque(false);
        coordinateXField = new JTextField();
        coordinateXField.setPreferredSize(new Dimension(50, 20));
        coordinateXErrMsg = new JLabel();
        coordinateXErrMsg.setFont(new Font("Helvetica", Font.ITALIC, 11));
        coordinateXPanel.add(coordinateXField);
        coordinateXPanel.add(coordinateXErrMsg);

        JPanel coordinateYPanel = new JPanel();
        coordinateYPanel.setLayout(new BoxLayout(coordinateYPanel, BoxLayout.Y_AXIS));
        coordinateYPanel.setOpaque(false);
        coordinateYField = new JTextField();
        coordinateYField.setPreferredSize(new Dimension(50, 20));
        coordinateYErrMsg = new JLabel();
        coordinateYErrMsg.setFont(new Font("Helvetica", Font.ITALIC, 11));
        coordinateYPanel.add(coordinateYField);
        coordinateYPanel.add(coordinateYErrMsg);

        JPanel viewUpdate = OrganizationView.createGUI(namePanel, typeField, annualTurnoverPanel,
                postalAddressPanel, null, null, coordinateXPanel, coordinateYPanel);

        viewUpdate.setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(viewUpdate);
        setOpaque(false);
    }

    public void show(Organization organization) {
        if (organization != null) {
            nameField.setText(organization.getName());
            nameField.setAlignmentX(SwingConstants.LEADING);

            int index = organization.getType().ordinal();
            typeField.setSelectedItem(index);

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            annualTurnoverField.setText(numberFormat.format(organization.getAnnualTurnover()));
            if (organization.getPostalAddress() != null) {
                postalAddressField.setText(organization.getPostalAddress().getZipCode());
            } else {
                postalAddressField.setText(null);
            }

            coordinateXField.setText(numberFormat.format(organization.getCoordinates().getX()));
            coordinateYField.setText(numberFormat.format(organization.getCoordinates().getY()));
        }
        else {
            nameField.setText("Name");
            typeField.setSelectedItem(0);
            annualTurnoverField.setText("0");
            postalAddressField.setText(null);
            coordinateXField.setText("0");
            coordinateYField.setText("0");
        }
        this.organization = organization;
    }

    public Organization getOrganization() {
        boolean flag = true;

        String name = null;
        try {
            name = OrganizationBuilder.getName(new Scanner(nameField.getText()));
            nameErrMsg.setVisible(false);
        } catch (InvalidArgumentException e) {
            nameErrMsg.setVisible(true);
            nameErrMsg.setText(e.getMessage());
            flag = false;
        }
        double annualTurnover = 0.0;
        try {
            annualTurnover = OrganizationBuilder.getAnnualTurnover(new Scanner(annualTurnoverField.getText()));
            annualTurnoverErrMsg.setVisible(false);
        } catch (InvalidArgumentException e) {
            annualTurnoverErrMsg.setVisible(true);
            annualTurnoverErrMsg.setText(e.getMessage());
            flag = false;
        }

        Address postalAddress = null;
        try {
            if (!postalAddressField.getText().isBlank()) {
                postalAddress = OrganizationBuilder.getAddress(new Scanner(postalAddressField.getText()));
            }
            postalAddressErrMsg.setVisible(false);
        } catch (InvalidArgumentException e) {
            postalAddressErrMsg.setVisible(true);
            postalAddressErrMsg.setText(e.getMessage());
            flag = false;
        }

        int x = 0;
        try {
            x = OrganizationBuilder.getCoordinatesX(new Scanner(coordinateXField.getText()));
            coordinateXErrMsg.setVisible(false);
        } catch (InvalidArgumentException e) {
            coordinateXErrMsg.setVisible(true);
            coordinateXErrMsg.setText(e.getMessage());
            flag = false;
        }

        long y = 0;
        try {
            y = OrganizationBuilder.getCoordinatesY(new Scanner(coordinateYField.getText()));
            coordinateYErrMsg.setVisible(false);
        } catch (InvalidArgumentException e) {
            coordinateYErrMsg.setVisible(true);
            coordinateYErrMsg.setText(e.getMessage());
            flag = false;
        }

        OrganizationType type = null;
        try {
            type = OrganizationBuilder.getOrganizationType(new Scanner(String.valueOf(typeField.getSelectedItem())));
        } catch (InvalidArgumentException ignored) {
            flag = false;
        }

        if (flag) {
            Organization.OrganizationBuilder builder = Organization.builder()
                    .name(name)
                    .annualTurnover(annualTurnover)
                    .coordinates(new Coordinates(x, y))
                    .type(type)
                    .postalAddress(postalAddress);
            if (organization != null) {
                builder.id(organization.getId());
            }

            return builder.build();
        }
        else {
            return null;
        }
    }
}
