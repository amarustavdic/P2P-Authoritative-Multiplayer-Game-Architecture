package com.mygame.app.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class PlayerInfo extends JPanel {
    private RoundButton playerColor;
    private JLabel playerName;

    public PlayerInfo() {
        setBackground(Color.decode("#0A0908"));
        setLayout(new FlowLayout());



        Image playerImage = null;
        try {
            playerImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/no-profile-picture-icon.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon playerImageContainer = new ImageIcon(playerImage);
        JLabel profilePicture = new JLabel(playerImageContainer);
        profilePicture.setPreferredSize(new Dimension(30,30));
        playerName = new JLabel("Player Name");
        playerName.setFont(new Font("Arial",Font.BOLD, 20));
        playerName.setForeground(Color.decode("#EAE0D5"));
        playerName.setPreferredSize(new Dimension(270,30));      // lower width here if in case of adding other components

        playerColor = new RoundButton(10);
        playerColor.setFocusable(false);
        playerColor.setBackground(Color.decode("#C6AC8F"));

        Clock clock = new Clock();
        clock.setPreferredSize(new Dimension(60,30));

        add(profilePicture, FlowLayout.LEFT);
        add(playerName);
        add(playerColor);
        add(clock);


    }


    public void setUp(char color, String name) {
        switch (color) {
            case 'r':
                playerColor.setBackground(Color.RED);
                break;
            case 'y':
                playerColor.setBackground(Color.YELLOW);
                break;
            default:
                playerColor.setBackground(Color.decode("#C6AC8F"));
                break;
        }
        playerName.setText(name);
    }

    public RoundButton getPlayerColor() {
        return playerColor;
    }
}
