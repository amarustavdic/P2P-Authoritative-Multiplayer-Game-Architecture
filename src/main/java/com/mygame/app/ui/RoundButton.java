package com.mygame.app.ui;
import java.awt.*;
import javax.swing.*;
public class RoundButton extends JButton {
    private int radius;

    public RoundButton(int radius) {
        this.radius = radius;
        setOpaque(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int diameter = radius * 2;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillOval(x, y, diameter, diameter);
        g2d.dispose();

        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(radius * 2, radius * 2);
    }

}
