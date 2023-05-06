package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Board extends JComponent {

    private static final int DRAG_SIZE_CHANGE = 10;
    private Point mousePosition;

    ArrayList<Ship> ships = new ArrayList<>();


    private final BufferedImage buffer;

    private int fontSize = 20;
    private int tileWidth = 32;
    private int tileHeight = 32;
    private int spacing = 2;
    private int width = tileWidth*11+spacing*13+300;
    private int height = tileHeight*11+spacing*15;







    public Board() {
        this.buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(width,height));

        ships.add(new Ship(100, 100, 3*(tileWidth+spacing), tileHeight));
        ships.add(new Ship(200, 200, 2*(tileWidth+spacing), tileHeight));



        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePosition = e.getPoint();
                // for more ships quick test
                for (Ship ship : ships) {
                    if (ship.contains(mousePosition)) {
                        setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        int newWidth = ship.width - DRAG_SIZE_CHANGE;
                        int newHeight = ship.height - DRAG_SIZE_CHANGE;
                        ship.setSize(newWidth, newHeight);
                        ship.setLocation(ship.x+DRAG_SIZE_CHANGE/2, ship.y+DRAG_SIZE_CHANGE/2);
                        ship.isDragging = true;
                        repaint();
                        break;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                mousePosition = e.getPoint();
                for (Ship ship : ships) {
                    if (ship.contains(mousePosition)) {
                        ship.isDragging = false;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        int snapX = snapToGrid(ship.x, tileWidth + spacing);
                        int snapY = snapToGrid(ship.y, tileHeight + spacing);
                        ship.setLocation(snapX, snapY);
                        ship.setSize(ship.width+DRAG_SIZE_CHANGE, ship.height+DRAG_SIZE_CHANGE);
                        repaint();
                    }
                }
            }

            private int snapToGrid(int coordinate, int gridSpacing) {
                return (coordinate + gridSpacing/2) / gridSpacing * gridSpacing;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                mousePosition = e.getPoint();
                for (Ship ship : ships) {
                    if (ship.isDragging) {
                        ship.x += e.getX() - mousePosition.x;
                        ship.y += e.getY() - mousePosition.y;
                        mousePosition.x = e.getX();
                        mousePosition.y = e.getY();
                    }
                }
                repaint();
            }

        });
        
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // getting graphics from buffer, to render some additional things
        Graphics2D bufferedGraphics = buffer.createGraphics();
        bufferedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bufferedGraphics.setColor(new Color(208, 236, 255));
        bufferedGraphics.fillRect(0,0,width,height);


        drawTiles(bufferedGraphics);
        drawCoordinates(bufferedGraphics);



        for (Ship ship : ships) {
            bufferedGraphics.setColor(Color.GRAY);
            bufferedGraphics.fillRoundRect(ship.x, ship.y, ship.width-spacing, ship.height, 10, 15);
        }





        // drawing off-screen buffer onto the screen
        g2.drawImage(buffer, 0, 0, null);
        bufferedGraphics.dispose();
    }



    private void drawCoordinates(Graphics2D bufferedGraphics) {

        bufferedGraphics.setColor(new Color(47, 47, 47));
        Font font = new Font("Impact", Font.BOLD, fontSize);
        bufferedGraphics.setFont(font);
        // coordinates for columns
        for (int i = 0; i < 10; i++) {
            String coord = Character.toString((char) ('A' + i));
            int x = (tileWidth+spacing) * (i+1) + (tileWidth - bufferedGraphics.getFontMetrics().stringWidth(coord)) / 2;
            int y = tileHeight - (tileHeight - font.getSize()) / 2 - bufferedGraphics.getFontMetrics().getDescent();
            bufferedGraphics.drawString(coord, x, y);
        }

        // coordinates for rows
        for (int i = 0; i < 10; i++) {
            String coord = Integer.toString(i+1);
            int x = (tileWidth - bufferedGraphics.getFontMetrics().stringWidth(coord)) / 2;
            int y = (tileHeight+spacing) * (i+2) - (tileHeight - font.getSize()) / 2 - bufferedGraphics.getFontMetrics().getDescent();
            bufferedGraphics.drawString(coord, x, y);
        }
    }

    private void drawTiles(Graphics2D bufferedGraphics) {
        bufferedGraphics.setColor(new Color(145, 176, 253));
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                int x = i * (tileWidth + spacing);
                int y = j * (tileHeight + spacing);
                bufferedGraphics.fillRoundRect(x, y, tileWidth, tileHeight, 10, 15);
            }
        }
    }
}
