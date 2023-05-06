package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Menu extends JComponent {

    private final BufferedImage buffer;

    public Menu(int width, int height) {
        this.setPreferredSize(new Dimension(width,height));
        this.buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // getting graphics from buffer, to render some additional things
        Graphics2D bufferedGraphics = buffer.createGraphics();
        bufferedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bufferedGraphics.setColor(new Color(208, 236, 255));





        // drawing off-screen buffer onto the screen
        g2.drawImage(buffer, 0, 0, null);
        bufferedGraphics.dispose();
    }



}
