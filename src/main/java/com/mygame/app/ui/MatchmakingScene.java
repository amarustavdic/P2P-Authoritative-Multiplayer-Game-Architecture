package com.mygame.app.ui;


import com.google.common.eventbus.EventBus;
import com.mygame.app.ui.ebus.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchmakingScene extends JPanel {

    private EventBus eventBus;
    private Dot[] dots;

    public MatchmakingScene(int width, int height, EventBus eventBus) {
        this.eventBus = eventBus;

        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(33, 33, 33));
        this.setLayout(new GridBagLayout());



        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        dots = new Dot[6];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new Dot();
            dots[i].setSize(new Dimension(50,50));
            dots[i].setPreferredSize(new Dimension(50, 50));

            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(dots[i], gbc);
        }

        AtomicInteger dotIndex = new AtomicInteger();
        Random rnd = new Random();
        Timer animationTimer = new Timer(500, e -> {
            if (dotIndex.get() > dots.length-1) dotIndex.set(0);
            int r = rnd.nextInt(255);
            int g = rnd.nextInt(255);
            int b = rnd.nextInt(255);

            dots[dotIndex.get()].setColor(new Color(r,g,b));
            dots[dotIndex.get()].repaint();
            dotIndex.set(dotIndex.get()+1);
        });
        animationTimer.start();



        Timer test = new Timer(10000, e -> {
            publishEvent(EventType.MATCH_FOUND);
        });
        test.start();
    }

    private void publishEvent(EventType eventType) {
        eventBus.post(eventType);
    }
}



class Dot extends JComponent {

    private Color color = Color.BLUE;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.fillOval(0,0,getWidth(),getHeight());
    }


    public void setColor(Color color) {
        this.color = color;
    }
}
