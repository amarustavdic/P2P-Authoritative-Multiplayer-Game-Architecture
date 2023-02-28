package com.mygame.app.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class GamePanel extends JPanel {
    private final ContainerPanel containerPanel;
    private PlayerInfo localPlayer;
    private PlayerInfo otherPlayer;
    private Droppers droppers;
    private Chat chat;

    public GamePanel(ContainerPanel containerPanel) {
        this.containerPanel = containerPanel;
        setLayout(new GridBagLayout());
        setBackground(Color.GREEN);

        GridBagConstraints gbc = new GridBagConstraints();
        localPlayer = new PlayerInfo();
        localPlayer.setPreferredSize(new Dimension(400,40));
        otherPlayer = new PlayerInfo();
        otherPlayer.setPreferredSize(new Dimension(400, 40));
        GameGrid gameGrid = new GameGrid();
        gameGrid.setPreferredSize(new Dimension(400,330));
        gameGrid.setBorder(new LineBorder(Color.BLACK, 1));
        droppers = new Droppers(gameGrid, localPlayer, otherPlayer);
        droppers.setPreferredSize(new Dimension(400,55));
        droppers.setBorder(new LineBorder(Color.BLACK, 1));


        JPanel otherControls = new JPanel();
        otherControls.setLayout(new BorderLayout());
        otherControls.setBackground(Color.decode("#0A0908"));
        otherControls.setPreferredSize(new Dimension(240,465));

        chat = new Chat();
        chat.setPreferredSize(new Dimension(230, 180));
        chat.setBorder(new EmptyBorder(0, 10, 0, 10));
        otherControls.add(chat, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.gridheight = 1;
        add(otherPlayer, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 7;
        add(droppers, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 7;
        gbc.gridheight = 6;
        add(gameGrid, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 7;
        gbc.gridheight = 1;
        add(localPlayer, gbc);

        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 10;
        add(otherControls, gbc);


    }




    public void setUpLocalPlayer(char color, String name) {
        localPlayer.setUp(color, name);
    }

    public void setUpOtherPlayer(char color, String name) {
        otherPlayer.setUp(color, name);
    }

    public void nextToMove(char color) {
        droppers.setNEXT_TURN(color);
    }

    public Chat getChat() {
        return chat;
    }
}
