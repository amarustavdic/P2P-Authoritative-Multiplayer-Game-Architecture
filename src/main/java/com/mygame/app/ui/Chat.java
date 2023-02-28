package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Chat extends JPanel {


    public Chat() {
        setBackground(Color.decode("#22333B"));
        setLayout(new BorderLayout());

        JTextField inputField = new JTextField("Type message here...");
        inputField.setBackground(Color.decode("#22333B"));
        inputField.setForeground(Color.decode("#EAE0D5"));
        inputField.setBorder(null);
        inputField.setPreferredSize(new Dimension(250,30));
        inputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals("Type message here...")) {
                    inputField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        add(inputField, BorderLayout.SOUTH);




    }


}
