package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;

public class GameGrid extends JPanel {


    public GameGrid() {
        setBackground(Color.BLACK);
        GridLayout gridLayout = new GridLayout(6,7);
        gridLayout.setVgap(0);
        gridLayout.setHgap(0);
        setLayout(gridLayout);



        RoundPanel[][] cells = new RoundPanel[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                cells[i][j] = new RoundPanel(20);
                cells[i][j].setBackground(Color.RED);
                add(cells[i][j]);
            }
        }




    }





}
