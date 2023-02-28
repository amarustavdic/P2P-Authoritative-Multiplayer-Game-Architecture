package com.mygame.app.ui;

import com.mygame.app.game.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GameGrid extends JPanel {

    private RoundPanel[][] cells;
    private int animationRow;

    public GameGrid() {
        setBackground(Color.decode("#5E503F"));
        GridLayout gridLayout = new GridLayout(6,7);
        setLayout(gridLayout);



        char[][] color = GameLogic.getGameMtx();
        cells = new RoundPanel[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                cells[i][j] = new RoundPanel(20);
                switch (color[i][j]) {
                    case 'r':
                        cells[i][j].setBackground(Color.RED);
                        break;
                    case 'y':
                        cells[i][j].setBackground(Color.YELLOW);
                        break;
                    default:
                        cells[i][j].setBackground(Color.decode("#C6AC8F"));
                        break;
                }
                add(cells[i][j]);
            }
        }




    }



    public boolean animateMove(int j, char color) throws InterruptedException {
        Color c = getColor(color);
        animationRow = 0;
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationRow > 0) {
                    cells[animationRow-1][j].setBackground(Color.decode("#C6AC8F"));
                }
                cells[animationRow][j].setBackground(c);
                animationRow++;

                if (animationRow >= 6 || !Objects.equals(cells[animationRow][j].getBackground(), Color.decode("#C6AC8F"))) {
                    GameLogic.recordMove(animationRow, j, color);
                    GameLogic.print();
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.start();
        return true;
    }

    public Color getColor(char color) {
        if (color == 'r') return Color.RED;
        else {
            return Color.YELLOW;
        }
    }



}
