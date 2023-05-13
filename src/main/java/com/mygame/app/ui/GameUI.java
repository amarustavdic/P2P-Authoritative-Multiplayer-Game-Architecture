package com.mygame.app.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javax.swing.*;


public class GameUI extends JFrame {

    private final EventBus eventBus;
    private JComponent currentPanel;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;



    public GameUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        this.eventBus = new EventBus();
        eventBus.register(this);

        MainView mainView = new MainView(WIDTH, HEIGHT, eventBus);
        BoardSetupView boardSetupView = new BoardSetupView(WIDTH, HEIGHT, eventBus);

        currentPanel = mainView;
        this.setContentPane(currentPanel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    @Subscribe
    public void onPanelChange(EventMessage eventMessage) {
        if (eventMessage.getEventType().equals(EventType.SINGLE_PLAYER_BTN)) {
            currentPanel = new BoardSetupView(WIDTH, HEIGHT, eventBus);
            this.setContentPane(currentPanel);  // Update the content pane of the frame
        }
        revalidate();
        repaint();
    }
}
