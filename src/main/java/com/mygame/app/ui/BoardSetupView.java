package com.mygame.app.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BoardSetupView extends JPanel {
    private EventBus eventBus;

    private static final int DRAG_SIZE_CHANGE = 10;
    private Point mousePosition;
    ArrayList<Ship> ships = new ArrayList<>();

    private final Color shipColor = new Color(131, 131, 131);
    private final Color shipDarkColor = new Color(80, 80, 80);
    private final int shipTileWidth = 40;
    private final int shipTileHeight = 40;
    private int fontSize = 20;
    private int tileWidth = 40;
    private int tileHeight = 40;
    private int spacing = 2;
    private int width;
    private int height;


    public BoardSetupView(int with, int height, EventBus eventBus) {
        this.width = with;
        this.height = height;
        this.eventBus = eventBus;
        eventBus.register(this);  // subscribe to eventbus


        ships.add(new Ship(tileWidth*13, tileHeight, 4));
        ships.add(new Ship(tileWidth*17+spacing, tileHeight, 2));
        ships.add(new Ship(tileWidth*13, 2*tileHeight+spacing, 1));
        ships.add(new Ship(tileWidth*14+spacing, 2*tileHeight+spacing, 5));
        ships.add(new Ship(tileWidth*13, 3*tileHeight+2*spacing, 3));
        ships.add(new Ship(tileWidth*16+spacing, 3*tileHeight+2*spacing, 3));








        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePosition = e.getPoint();
                // for more ships quick test
                for (Ship ship : ships) {
                    if (ship.contains(mousePosition)) {
                        setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        ship.isDragging = true;
                        repaint();
                        break;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                mousePosition = e.getPoint();
                for (Ship ship : ships) {
                    if (ship.isDragging) {
                        ship.isDragging = false;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        int snapX = snapToGrid(ship.x, tileWidth + spacing);
                        int snapY = snapToGrid(ship.y, tileHeight + spacing);
                        ship.setLocation(snapX, snapY);
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
                        ship.x = mousePosition.x;
                        ship.y = mousePosition.y;
                        break;
                    }
                }
                repaint();
            }
        });

    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // getting graphics from buffer, to render some additional things
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(208, 236, 255));
        g2d.fillRect(0,0,width,height);


        drawTiles(g2d);
        drawCoordinates(g2d);
        drawShipyard(g2d);





        /*
        for (Ship ship : ships) {
            g2d.setColor(Color.GRAY);
            g2d.fillRoundRect(ship.x, ship.y, ship.width-spacing, ship.height, 15, 15);
        }

         */



        g2d.dispose();
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
                bufferedGraphics.fillRoundRect(x, y, tileWidth, tileHeight, 15, 15);
            }
        }
    }

    private void drawShipyard(Graphics2D g) {
        Font font = new Font("Impact", Font.BOLD, fontSize);
        int y = tileHeight - (tileHeight - font.getSize()) / 2 - g.getFontMetrics().getDescent();
        g.drawString("SHIPYARD",590, y);

        // drawing ships in shipyard
        for (Ship ship : ships) {
            ship.width = ship.length*(shipTileWidth+spacing)-spacing;
            ship.height = shipTileHeight;
            ship.tileWidth = shipTileWidth;
            ship.tileHeight = tileHeight;

            g.setColor(shipColor);
            g.fillRoundRect(ship.x, ship.y, ship.width, ship.height,15,15);
            g.setColor(shipDarkColor);
            for (int i = 0; i < ship.length; i++) {
                int shipDetailX = (i * ship.tileWidth) + ship.x + ship.tileWidth/4;
                int shipDetailY = ship.tileHeight/4 + ship.y;
                g.fillOval(shipDetailX, shipDetailY, ship.tileWidth/2, ship.tileHeight/2);
            }
        }

    }





    // handling messages from eventbus
    @Subscribe
    public void handleMessage(EventMessage eventMessage) {

    }
}
