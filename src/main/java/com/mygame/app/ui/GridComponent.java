package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;

public class GridComponent extends JComponent {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private int width;
    private int height;
    private int tileSideLength = 40;
    private int spacing = 2;
    private int fontSize = 25;


    public GridComponent(int x, int y, int tileSideLength) {
        screenX = x;
        screenY = y;
        this.width = 11 * (tileSideLength + spacing) - spacing;
        this.height = width;

        setBounds(x, y, width, height);
        setOpaque(false);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawCoordinates(g2);
        drawTiles(g2);
    }




    private void drawCoordinates(Graphics2D g) {
        g.setColor(GUIConstants.FONT_COLOR);
        Font font = new Font("Impact", Font.BOLD, fontSize);
        g.setFont(font);
        // coordinates for columns
        for (int i = 0; i < 10; i++) {
            String coord = Character.toString((char) ('A' + i));
            int x = (tileSideLength+spacing) * (i+1) + (tileSideLength - g.getFontMetrics().stringWidth(coord)) / 2;
            int y = tileSideLength - (tileSideLength - font.getSize()) / 2 - g.getFontMetrics().getDescent();
            g.drawString(coord, x, y);
        }

        // coordinates for rows
        for (int i = 0; i < 10; i++) {
            String coord = Integer.toString(i+1);
            int x = (tileSideLength - g.getFontMetrics().stringWidth(coord)) / 2;
            int y = (tileSideLength+spacing) * (i+2) - (tileSideLength - font.getSize()) / 2 - g.getFontMetrics().getDescent();
            g.drawString(coord, x, y);
        }
    }


    private void drawTiles(Graphics2D bufferedGraphics) {
        bufferedGraphics.setColor(GUIConstants.BOARD_TILE_COLOR);
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                int x = i * (tileSideLength + spacing);
                int y = j * (tileSideLength + spacing);
                bufferedGraphics.fillRoundRect(x, y, tileSideLength, tileSideLength, 20, 20);
            }
        }
    }
}
