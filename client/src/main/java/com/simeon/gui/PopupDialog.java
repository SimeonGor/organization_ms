package com.simeon.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;

public class PopupDialog extends JDialog {
    private JLabel messageLabel;
    public PopupDialog(JFrame parent) {
        super(parent, "Message");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(createGUI());

        pack();

        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }

    private JPanel createGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        messageLabel = new JLabel();
        panel.add(messageLabel);

        JButton okButton = new JButton("OK");

        okButton.addActionListener(e -> {
            setVisible(false);
            messageLabel.setText(null);
        });
        panel.add(okButton);

        return panel;
    }

    public void show(String message) {
        messageLabel.setText(message);
        messageLabel.repaint();
        setVisible(true);
    }
}
