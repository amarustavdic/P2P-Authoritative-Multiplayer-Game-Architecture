package com.mygame.app.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ShipComponent extends JComponent {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;
    private volatile int prevX = 0;
    private volatile int prexY = 0;
    private int shipWidth = 80;
    private int shipHeight = 80;
    private int arcWidth = 20; // Round rectangle arc width
    private int arcHeight = 20; // Round rectangle arc height

    private int tileSideLength = 40;
    private int spacing = 2;
    private int shipLength = 0;

    private boolean isNormalOrientation = true;
    private boolean wasDragged = false;
    private boolean[] hitVector;



    private ShipComponent me;

    public ShipComponent(int x, int y, int shipLength) {
        screenX = x;
        screenY = y;
        this.shipLength = shipLength;
        this.shipWidth = shipLength * (tileSideLength + spacing) - spacing;
        this.shipHeight = tileSideLength;
        this.hitVector = new boolean[shipLength];
        //setBorder(new LineBorder(Color.BLUE, 3));
        //setBackground(Color.WHITE);
        setBounds(x, y, shipWidth, shipHeight);
        setOpaque(false);

        // this needs to be done properly, NOT LIKE THIS!!!, it was a quick fix only
        this.me = this;








        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right button is pressed
                    int temp = shipWidth;
                    shipWidth = shipHeight;
                    shipHeight = temp;

                    // Update the bounds of the component with the new width and height
                    setBounds(getX(), getY(), shipWidth, shipHeight);

                    isNormalOrientation = !isNormalOrientation;
                    // Repaint the component to reflect the changes
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = prevX = getX();
                myY = prexY = getY();


                // Bring the component to the front
                Container parent = getParent();
                parent.setComponentZOrder(me, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!wasDragged) {
                    return;
                }
                Container parent = getParent();

                // Check if the released position is above the GridComponent
                Component[] components = parent.getComponents();
                for (Component component : components) {
                    if (component instanceof GridComponent) {
                        GridComponent grid = (GridComponent) component;

                        // Calculate the snapped position
                        int shipCenterX = getX() - grid.getX();
                        int shipCenterY = getY() - grid.getY();

                        if (grid.gridContains(shipCenterX, shipCenterY)) {
                            int realX = getX() - grid.getX();
                            int realY = getY() - grid.getY();
                            int snapX = (realX / (tileSideLength + grid.getSpacing())) * (tileSideLength + grid.getSpacing()) + grid.getX();
                            int snapY = (realY / (tileSideLength + grid.getSpacing())) * (tileSideLength + grid.getSpacing()) + grid.getY();
                            // check if tail is also inside the grid

                            int shipTailPoint;
                            if (isNormalOrientation) shipTailPoint = snapX + shipWidth;
                            else shipTailPoint = realY + shipHeight;

                            if (grid.gridContains(snapX, shipTailPoint-(tileSideLength/2))) {
                                setLocation(snapX, snapY);
                            } else {
                                setLocation(prevX, prexY);
                            }
                            break;
                        } else {
                            // set previous location
                            setLocation(prevX, prexY);
                        }
                        wasDragged = false;
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
                wasDragged = true;
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

        g2Buffer.setColor(GUIConstants.SHIP_SECONDARY_COLOR);
        g2Buffer.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        for (int i = 0; i < shipLength; i++) {
            if (!hitVector[i]) {
                g2Buffer.setColor(GUIConstants.SHIP_PRIMARY_COLOR);
            } else {
                g2Buffer.setColor(GUIConstants.SHIP_HIT_COLOR);
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


        g2Buffer.setColor(Color.RED);
        g2Buffer.fillRect(0,0,5,5);

        // Draw the buffer onto the component using g2
        g.drawImage(buffer, 0, 0, null);
    }



    public void setHitVector(boolean[] hitVector) {
        this.hitVector = hitVector;
    }

    public int getShipWidth() {
        return shipWidth;
    }

    public int getShipHeight() {
        return shipHeight;
    }
}

