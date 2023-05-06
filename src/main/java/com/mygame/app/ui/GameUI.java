package com.mygame.app.ui;

import javax.swing.*;


public class GameUI extends JFrame {


    public GameUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        MainView mainView = new MainView(800, 550);


        this.add(mainView);

        this.pack();
        this.setVisible(true);

    }
}
