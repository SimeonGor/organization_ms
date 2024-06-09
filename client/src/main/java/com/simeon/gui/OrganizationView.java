package com.simeon.gui;

import com.simeon.Response;
import com.simeon.ResponseHandler;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class OrganizationView {
    private static final ResourceBundle lang = ResourceBundle.getBundle("lang");
    private static JPanel createPanel(String name, JComponent component) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        JLabel label = new JLabel(name);
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

    protected static JPanel createGUI(
            JComponent name,
            JComponent type,
            JComponent annualTurnover,
            JComponent postalAddress,
            JComponent creationDate,
            JComponent user,
            JComponent coordinationX,
            JComponent coordinationY) {
        JPanel panel = new JPanel();

        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setOpaque(false);
        namePanel.add(name);
        namePanel.add(Box.createHorizontalGlue());
        namePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(namePanel);

        panel.add(createPanel(lang.getString("type"), type));
        panel.add(createPanel(lang.getString("annualTurnover"), annualTurnover));
        panel.add(createPanel(lang.getString("postalAddress"), postalAddress));

        panel.add(Box.createVerticalGlue());

        if (creationDate != null) {
            panel.add(createPanel(lang.getString("creationDate"), creationDate));
        }
        if (user != null) {
            panel.add(createPanel(lang.getString("user"), user));
        }

        JPanel coordPanel = new JPanel();
        LayoutManager coordPanelLayout = new BoxLayout(coordPanel, BoxLayout.Y_AXIS);
        coordPanel.setLayout(coordPanelLayout);
        coordPanel.setOpaque(false);

        coordPanel.add(createPanel("x:", coordinationX));
        coordPanel.add(createPanel("y:", coordinationY));

        panel.add(createPanel(lang.getString("coordinates"), coordPanel));

        return panel;
    }
}
