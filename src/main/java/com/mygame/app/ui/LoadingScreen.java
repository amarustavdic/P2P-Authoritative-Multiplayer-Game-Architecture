package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;


public class LoadingScreen extends JComponent {

    private int loadingBarX = 166;
    private int loadingBarY = 384;
    private int loadingBarWidth = 20;
    private int loadingBarMaxWidth = 707;



    public LoadingScreen(int width, int height) {
        this.setPreferredSize(new Dimension(width,height));
        this.setSize(new Dimension(width,height));


        // just for test now, can be optimized later
        Timer loadingBarTimer = new Timer(10, e -> {
            if (loadingBarWidth < loadingBarMaxWidth) {
                loadingBarWidth++;
                repaint(loadingBarX,loadingBarY,loadingBarWidth,50);
            }
        });
        loadingBarTimer.start();


    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,getWidth(),getHeight());


        String text1 = "THE CLASSIC NAVAL COMBAT GAME";
        Font font1 = new Font("Arial", Font.BOLD, 25);
        FontMetrics metrics1 = g2.getFontMetrics(font1);
        int x1 = (getWidth() - metrics1.stringWidth(text1)) / 2;
        int y1 = ((getHeight() - metrics1.getHeight()) / 2) + metrics1.getAscent() - 140;
        g2.setFont(font1);
        g2.setColor(new Color(220, 161, 12));
        g2.drawString(text1, x1, y1);

        // draw string here on center of the screen
        String text = "BATTLESHIP";
        Font font = new Font("Arial", Font.BOLD, 100);
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent() - 70;
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        String text2 = "DECENTRALIZED EDITION";
        Font font2 = new Font("Arial", Font.BOLD, 20);
        FontMetrics metrics2 = g2.getFontMetrics(font2);
        int x2 = (getWidth() - metrics2.stringWidth(text2)) / 2;
        int y2 = ((getHeight() - metrics2.getHeight()) / 2) + metrics2.getAscent();
        g2.setFont(font2);
        g2.setColor(new Color(14, 14, 175));
        g2.drawString(text2, x2, y2);


        // draw the loading screen bar
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(loadingBarX, loadingBarY,loadingBarMaxWidth, 50, 30, 30);
        g2.setColor(Color.RED);
        g2.fillRoundRect(loadingBarX, loadingBarY,loadingBarWidth, 50, 30, 30);


        drawStringCenter(g2,"Loading...",-140,Color.WHITE,20);
    }


    private void drawStringCenter(Graphics2D g2, String text, int offset, Color color, int fontSize) {
        Font font = new Font("Arial", Font.BOLD, fontSize);
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent() - offset;
        g2.setFont(font);
        g2.setColor(color);
        g2.drawString(text, x, y);
    }


}

