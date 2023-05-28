package com.myproject.game.ui;

import com.myproject.game.ebus.EventType;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;


public class GameGUI extends JFrame {

    private EventBus eventBus;
    private int WIDTH = 600;
    private int HEIGHT = 400;

    private GameGUI gameGUI;
    private GameScene1 gameScene1;
    private GameScene2 gameScene2;


    public GameGUI(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
        this.gameGUI = this;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        gameScene1 = new GameScene1(WIDTH, HEIGHT, eventBus);

        this.setContentPane(gameScene1);

        this.pack();
        this.setVisible(true);
    }



    @Subscribe
    public void handleMessage(EventType eventType) {

        if (eventType == EventType.ONLINE_GAME) {
            MatchmakingScene matchmakingScene = new MatchmakingScene(WIDTH, HEIGHT, eventBus);
            gameGUI.setContentPane(matchmakingScene);

        } else if (eventType == EventType.MAIN_MENU) {
            gameGUI.setContentPane(gameScene1);

        } else if (eventType == EventType.MATCH_FOUND) {
            gameScene2 = new GameScene2(WIDTH, HEIGHT, eventBus);
            gameGUI.setContentPane(gameScene2);
        }
    }


}
