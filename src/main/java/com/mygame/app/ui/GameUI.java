package com.mygame.app.ui;

import javax.swing.*;


public class GameUI extends JFrame {


    public GameUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);


        //LoadingScreen loadingScreen = new LoadingScreen(1020,640);
        //this.add(loadingScreen);

        //Menu menu = new Menu(1020,640);
        //this.add(menu);


        this.add(new Board());


        this.pack();
        this.setVisible(true);

    }
}
