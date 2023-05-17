package com.myproject.game.ui;
import com.myproject.game.ebus.EventType;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameScene1 extends JPanel {

    private EventBus eventBus;

    public GameScene1(int width, int height, EventBus eventBus) {
        this.eventBus = eventBus;

        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(33, 33, 33));

        Font titleFont = new Font("Arial", Font.BOLD, 50);

        JLabel titleLabel = new JLabel("TIC-TAC-TOE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        JButton button1 = new JButton("Online");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                publishEvent(EventType.ONLINE_GAME);
            }
        });
        JButton button2 = new JButton("Versus bot");

        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(200, 80);
        button1.setPreferredSize(buttonSize);
        button2.setPreferredSize(buttonSize);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 6;
        add(button1, gbc);

        gbc.gridy = 4;
        add(button2, gbc);
    }



    private void publishEvent(EventType eventType) {
        eventBus.post(eventType);
    }
}
