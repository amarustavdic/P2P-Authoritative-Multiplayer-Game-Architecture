package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {

    private JButton[][] buttons;


    public Board(int width, int height) {
        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(176, 176, 176));
        this.setLayout(new GridBagLayout());


        Font btnsFont = new Font("Arial", Font.BOLD, 30);
        GridBagConstraints gbc = new GridBagConstraints();
        buttons = new JButton[3][3];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFocusable(false);
                buttons[i][j].setBackground(new Color(17, 17, 17));
                buttons[i][j].setForeground(Color.WHITE);
                buttons[i][j].setPreferredSize(new Dimension(90,90));
                buttons[i][j].setFont(btnsFont);
                buttons[i][j].setFocusPainted(false); // Disable focus painting
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton btn = (JButton) e.getSource();
                        btn.setText("X");
                    }
                });

                gbc.gridx = i;
                gbc.gridy = j;
                this.add(buttons[i][j], gbc);
            }
        }
    }



}
