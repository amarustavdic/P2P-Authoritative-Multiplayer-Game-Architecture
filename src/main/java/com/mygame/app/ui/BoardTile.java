package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;

public class BoardTile extends JComponent {



    public BoardTile() {

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.RED);
        g2d.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 15,15);
    }
}
