package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;

public class RoundButton extends JButton {
    private String text;

    public RoundButton(String text) {
        this.text = text;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!getModel().isArmed()) {
            g2.setColor(GUIConstants.PRIMARY_BUTTON_COLOR);
        } else {
            g2.setColor(GUIConstants.SECONDARY_BUTTON_COLOR);
        }

        int width = getWidth();
        int height = getHeight();
        g2.fillRoundRect(0, 0, width, height, 20, 20);

        g2.setColor(GUIConstants.FONT_COLOR);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getAscent();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + textHeight;
        g2.drawString(text, x, y);
    }
}
