package com.mygame.app.ui;

import com.google.common.eventbus.EventBus;
import com.mygame.app.ui.ebus.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameScene2 extends JPanel {

    private EventBus eventBus;


    public GameScene2(int width, int height, EventBus eventBus) {
        this.eventBus = eventBus;

        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(33, 33, 33));
        this.setLayout(new GridBagLayout());



        Board board = new Board(270, 270);
        JButton abortBtn = new JButton("Abort");
        abortBtn.setPreferredSize(new Dimension(100, 90));
        abortBtn.setForeground(Color.WHITE);
        abortBtn.setBackground(new Color(17, 17, 17));
        abortBtn.setFocusable(false);
        abortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                publishEvent(EventType.MAIN_MENU);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        add(board, gbc);

        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        add(abortBtn, gbc);
    }



    private void publishEvent(EventType eventType) {
        eventBus.post(eventType);
    }
}
