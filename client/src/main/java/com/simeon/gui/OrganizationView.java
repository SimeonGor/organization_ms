package com.simeon.gui;

import com.simeon.Response;
import com.simeon.ResponseHandler;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.ResourceBundle;

public class OrganizationView extends JPanel {
    private static ResourceBundle lang = ResourceBundle.getBundle("lang");
    private HashMap<String, JLabel> labels = new HashMap<>();

    private JPanel createPanel(String name, JComponent component) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        JLabel label = new JLabel(lang.getString(name));
        labels.put(name, label);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(label);
        panel.add(Box.createHorizontalGlue());

        JPanel compPanel = new JPanel();
        compPanel.setOpaque(false);
        compPanel.setLayout(new BoxLayout(compPanel, BoxLayout.X_AXIS));
//        component.setBorder(null);
        compPanel.add(component);
        compPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(compPanel);
        panel.setOpaque(false);
        return panel;
    }

    public OrganizationView(JComponent name,
                            JComponent type,
                            JComponent annualTurnover,
                            JComponent postalAddress,
                            JComponent creationDate,
                            JComponent user,
                            JComponent coordinationX,
                            JComponent coordinationY) {
        createGUI(name, type, annualTurnover, postalAddress, creationDate, user, coordinationX, coordinationY);
    }

    protected void createGUI(
            JComponent name,
            JComponent type,
            JComponent annualTurnover,
            JComponent postalAddress,
            JComponent creationDate,
            JComponent user,
            JComponent coordinationX,
            JComponent coordinationY) {

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setOpaque(false);
        namePanel.add(name);
        namePanel.add(Box.createHorizontalGlue());
        namePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(namePanel);

        add(createPanel("type", type));
        add(createPanel("annualTurnover", annualTurnover));
        add(createPanel("postalAddress", postalAddress));

        add(Box.createVerticalGlue());

        if (creationDate != null) {
            add(createPanel("creationDate", creationDate));
        }
        if (user != null) {
            add(createPanel("user", user));
        }

        JPanel coordPanel = new JPanel();
        LayoutManager coordPanelLayout = new BoxLayout(coordPanel, BoxLayout.Y_AXIS);
        coordPanel.setLayout(coordPanelLayout);
        coordPanel.setOpaque(false);

        coordPanel.add(createPanel("x", coordinationX));
        coordPanel.add(createPanel("y", coordinationY));

        add(createPanel("coordinates", coordPanel));
    }

    public void relocale() {
        lang = ResourceBundle.getBundle("lang");
        for (var e : labels.entrySet()) {
            e.getValue().setText(lang.getString(e.getKey()));
        }
    }
}
