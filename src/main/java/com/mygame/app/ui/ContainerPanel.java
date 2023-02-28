package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;

public class ContainerPanel extends JPanel {

    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;
    private final CardLayout cardLayout;

    public ContainerPanel() {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        cardLayout = new CardLayout();

        setLayout(cardLayout);

        add(menuPanel, "menu");
        add(gamePanel, "game");

        cardLayout.show(this, "menu");
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
