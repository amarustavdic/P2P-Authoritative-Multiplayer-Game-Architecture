package com.mygame.app.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

public class ShipComponent extends JComponent {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;
    private int width = 80;
    private int height = 80;
    private int arcWidth = 20; // Round rectangle arc width
    private int arcHeight = 20; // Round rectangle arc height

    private int tileSideLength = 40;
    private int spacing = 2;
    private int shipLength = 0;

    private boolean isNormalOrientation = true;
    private boolean[] hitVector;



    private ShipComponent me;

    public ShipComponent(int x, int y, int shipLength) {
        screenX = x;
        screenY = y;
        this.shipLength = shipLength;
        this.width = shipLength * (tileSideLength + spacing) - spacing;
        this.height = tileSideLength;
        this.hitVector = new boolean[shipLength];
        //setBorder(new LineBorder(Color.BLUE, 3));
        //setBackground(Color.WHITE);
        setBounds(x, y, width, height);
        setOpaque(false);


        // this needs to be done properly, NOT LIKE THIS!!!, it was a quick fix only
        this.me = this;

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right button is pressed
                    int temp = width;
                    width = height;
                    height = temp;

                    // Update the bounds of the component with the new width and height
                    setBounds(getX(), getY(), width, height);

                    isNormalOrientation = !isNormalOrientation;
                    // Repaint the component to reflect the changes
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = getX();
                myY = getY();

                // Bring the component to the front
                Container parent = getParent();
                parent.setComponentZOrder(me, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Bring the component back to its original Z-order
                Container parent = getParent();
                //parent.setComponentZOrder(me, parent.getComponentCount() - 1);

                // Check if the released position is above the GridComponent
                Component[] components = parent.getComponents();
                for (Component component : components) {
                    if (component instanceof GridComponent) {
                        GridComponent grid = (GridComponent) component;

                        // Calculate the snapped position
                        // NEED SOME POLISHING!!!
                        int gX = grid.getX() + tileSideLength + spacing;
                        int gY = grid.getY() + tileSideLength + spacing;
                        int snapX = (getLocation().x - gX) / (tileSideLength + spacing) * (tileSideLength + spacing) + gX;
                        int snapY = (getLocation().y - gY) / (tileSideLength + spacing) * (tileSideLength + spacing) + gY;

                        // Set the snapped position
                        setLocation(snapX, snapY);
                        break;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }

        });

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                setLocation(myX + deltaX, myY + deltaY);
            }

            @Override
            public void mouseMoved(MouseEvent e) { }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Create a BufferedImage for double buffering
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2Buffer = buffer.createGraphics();

        // Paint on the buffer
        g2Buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2Buffer.setColor(new Color(47, 47, 47));
        g2Buffer.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        for (int i = 0; i < shipLength; i++) {
            if (!hitVector[i]) {
                g2Buffer.setColor(new Color(208, 208, 208));
            } else {
                g2Buffer.setColor(new Color(201, 0, 0));
            }
            int shipDetailX = 0;
            int shipDetailY = 0;
            if (isNormalOrientation) {
                shipDetailX = (i * (tileSideLength + spacing)) + tileSideLength/4;
                shipDetailY = tileSideLength/4;
            } else {
                shipDetailX = tileSideLength/4;
                shipDetailY = (i * (tileSideLength + spacing)) + tileSideLength/4;
            }

            g2Buffer.fillOval(shipDetailX, shipDetailY, tileSideLength/2, tileSideLength/2);
        }

        // Draw the buffer onto the component using g2
        g.drawImage(buffer, 0, 0, null);
    }



    public void setHitVector(boolean[] hitVector) {
        this.hitVector = hitVector;
    }
}

