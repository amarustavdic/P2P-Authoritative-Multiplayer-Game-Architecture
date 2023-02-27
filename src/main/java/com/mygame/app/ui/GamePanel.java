package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {
    private final ContainerPanel containerPanel;

    public GamePanel(ContainerPanel containerPanel) {
        this.containerPanel = containerPanel;
        setLayout(new GridBagLayout());
        setBackground(Color.GREEN);

        GridBagConstraints gbc = new GridBagConstraints();
        PlayerInfo localPlayer = new PlayerInfo();
        localPlayer.setPreferredSize(new Dimension(640,40));
        PlayerInfo otherPlayer = new PlayerInfo();
        otherPlayer.setPreferredSize(new Dimension(640, 40));
        Droppers droppers = new Droppers();
        droppers.setPreferredSize(new Dimension(400,55));
        GameGrid gameGrid = new GameGrid();
        gameGrid.setPreferredSize(new Dimension(400,330));
        JPanel otherControls = new JPanel();
        otherControls.setPreferredSize(new Dimension(240,385));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
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
        gbc.gridwidth = 10;
        gbc.gridheight = 1;
        add(localPlayer, gbc);

        gbc.gridx = 7;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 7;
        add(otherControls, gbc);


    }
}
