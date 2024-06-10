package com.simeon.gui;

import com.simeon.element.Organization;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class OrganizationInfo extends JPanel {
    private final JLabel name;
    private final JLabel type;
    private final JLabel annualTurnover;
    private final JLabel postalAddress;
    private final JLabel creationDate;
    private final JLabel user;
    private final JLabel coordinateX;
    private final JLabel coordinateY;
    @Getter
    private Organization organization;
    private final OrganizationView view;


    public OrganizationInfo() {
        name = new JLabel();
        name.setFont(new Font("Helvetica", Font.BOLD, 16));

        type = new JLabel();
//        type.setPreferredSize(new Dimension(100, 5));
        annualTurnover = new JLabel();
//        annualTurnover.setPreferredSize(new Dimension(100, 5));
        postalAddress = new JLabel();
//        postalAddress.setPreferredSize(new Dimension(100, 5));
        creationDate = new JLabel();
//        creationDate.setPreferredSize(new Dimension(100, 5));
        user = new JLabel();
//        user.setPreferredSize(new Dimension(50, 5));
        coordinateX = new JLabel();
//        coordinateX.setPreferredSize(new Dimension(50, 5));
        coordinateY = new JLabel();
//        coordinateY.setPreferredSize(new Dimension(50, 5));


        view = new OrganizationView(name, type, annualTurnover, postalAddress, creationDate,
                                    user, coordinateX, coordinateY);
        view.setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(view);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setBackground(Color.WHITE);
    }

    public void show(Organization organization) {
        if (organization != null) {
            name.setText(organization.getName());
            name.setAlignmentX(SwingConstants.LEADING);

            type.setText(organization.getType().toString());

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            annualTurnover.setText(numberFormat.format(organization.getAnnualTurnover()));
            if (organization.getPostalAddress() != null) {
                postalAddress.setText(organization.getPostalAddress().getZipCode());
            } else {
                postalAddress.setText(null);
            }

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            Date date = java.sql.Date.valueOf(organization.getCreationDate());
            creationDate.setText(dateFormat.format(date));
            user.setText(organization.getUserInfo().getUsername());

            coordinateX.setText(numberFormat.format(organization.getCoordinates().getX()));
            coordinateY.setText(numberFormat.format(organization.getCoordinates().getY()));
        }
        else {
            name.setText(null);
            type.setText(null);
            annualTurnover.setText(null);
            postalAddress.setText(null);
            creationDate.setText(null);
            coordinateX.setText(null);
            coordinateY.setText(null);
            user.setText(null);
        }
        name.repaint();
        type.repaint();
        annualTurnover.repaint();
        postalAddress.repaint();
        creationDate.repaint();
        coordinateX.repaint();
        coordinateY.repaint();
        user.repaint();
        view.repaint();
        this.repaint();
        this.organization = organization;
    }

    public void relocale() {
        view.relocale();
    }
}
