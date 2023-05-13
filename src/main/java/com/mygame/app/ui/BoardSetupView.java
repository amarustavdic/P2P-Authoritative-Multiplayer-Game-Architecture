package com.mygame.app.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BoardSetupView extends JPanel {
    private final EventBus eventBus;
    private final int boardTileSideLength = 40;
    private final int fontSize = 20;
    private final int spacing = 5;
    private final int width;
    private final int height;
    private final int repaintAreaMultiplier = 15;

    private Point mousePosition;
    private ArrayList<Ship> ships = new ArrayList<>();
    //                            x, y, shipyardWidth, shipyardHeight
    //private int[] shipyardArea = {,}


    public BoardSetupView(int with, int height, EventBus eventBus) {
        this.setBackground(new Color(208, 236, 255));
        this.width = with;
        this.height = height;
        this.setPreferredSize(new Dimension(with,height));
        generateShips();
        this.eventBus = eventBus;
        eventBus.register(this);  // subscribe to eventbus


        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePosition = e.getPoint();
                for (Ship ship : ships) {
                    if (ship.contains(mousePosition)) {
                        setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        ship.setDragging(true);
                        repaint(ship.getX() - repaintAreaMultiplier * spacing,
                                ship.getY() - repaintAreaMultiplier *spacing,
                                ship.getWidth() + 2 * repaintAreaMultiplier * spacing,
                                ship.getHeight() + 2 * repaintAreaMultiplier * spacing);
                        break;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                mousePosition = e.getPoint();
                for (Ship ship : ships) {
                    if (ship.isDragging()) {
                        ship.setDragging(false);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        int snapX = snapToGrid(ship.getX(), boardTileSideLength + spacing);
                        int snapY = snapToGrid(ship.getY(), boardTileSideLength + spacing);
                        ship.setLocation(snapX, snapY);
                        repaint(ship.getX() - repaintAreaMultiplier * spacing,
                                ship.getY() - repaintAreaMultiplier * spacing,
                                ship.getWidth() + 2 * repaintAreaMultiplier * spacing,
                                ship.getHeight() + 2 * repaintAreaMultiplier * spacing);
                    }
                }
            }

            private int snapToGrid(int coordinate, int gridSpacing) {
                return (coordinate + gridSpacing/2) / gridSpacing * gridSpacing;
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                mousePosition = e.getPoint();
                for (Ship ship : ships) {
                    if (ship.isDragging()) {
                        ship.setX(mousePosition.x - ship.getWidth()/2);
                        ship.setY(mousePosition.y - ship.getHeight()/2);
                        repaint(ship.getX() - repaintAreaMultiplier * spacing,
                                ship.getY() - repaintAreaMultiplier * spacing,
                                ship.getWidth() + 2 * repaintAreaMultiplier * spacing,
                                ship.getHeight() + 2 * repaintAreaMultiplier * spacing);
                        break;
                    }
                }
            }
        });


        // experimenting with custom components
        BoardTile boardTile = new BoardTile();
        boardTile.setVisible(true);
        boardTile.setBounds(400,400,100,100);
        add(boardTile);



    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTiles(g2d);
        drawCoordinates(g2d);
        drawShipyard(g2d);
    }


    private void drawCoordinates(Graphics2D g) {
        g.setColor(new Color(47, 47, 47));
        Font font = new Font("Impact", Font.BOLD, fontSize);
        g.setFont(font);
        // coordinates for columns
        for (int i = 0; i < 10; i++) {
            String coord = Character.toString((char) ('A' + i));
            int x = (boardTileSideLength+spacing) * (i+1) + (boardTileSideLength - g.getFontMetrics().stringWidth(coord)) / 2;
            int y = boardTileSideLength - (boardTileSideLength - font.getSize()) / 2 - g.getFontMetrics().getDescent();
            g.drawString(coord, x, y);
        }

        // coordinates for rows
        for (int i = 0; i < 10; i++) {
            String coord = Integer.toString(i+1);
            int x = (boardTileSideLength - g.getFontMetrics().stringWidth(coord)) / 2;
            int y = (boardTileSideLength+spacing) * (i+2) - (boardTileSideLength - font.getSize()) / 2 - g.getFontMetrics().getDescent();
            g.drawString(coord, x, y);
        }
    }

    private void drawTiles(Graphics2D bufferedGraphics) {
        bufferedGraphics.setColor(new Color(145, 176, 253));
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                int x = i * (boardTileSideLength + spacing);
                int y = j * (boardTileSideLength + spacing);
                bufferedGraphics.fillRoundRect(x, y, boardTileSideLength, boardTileSideLength, 15, 15);
            }
        }
    }

    private void drawShipyard(Graphics2D g) {
        Font font = new Font("Impact", Font.BOLD, fontSize);
        int y = boardTileSideLength - (boardTileSideLength - font.getSize()) / 2 - g.getFontMetrics().getDescent();
        g.drawString("SHIPYARD",590, y);

        // drawing ships in shipyard
        for (Ship ship : ships) {

            g.setColor(ship.getShipColor());
            g.fillRoundRect(ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight(),15,15);
            g.setColor(ship.getHitMarkerColor());
            for (int i = 0; i < ship.getShipLength(); i++) {
                int markerOffset = boardTileSideLength + spacing;
                int vertOffset = boardTileSideLength / 4;
                int shipDetailX = (i * markerOffset) + ship.getX() + vertOffset;
                int shipDetailY = vertOffset + ship.getY();
                g.fillOval(shipDetailX, shipDetailY, boardTileSideLength/2, boardTileSideLength/2);
            }
        }

    }



    private void generateShips() {
        ships.add(new Ship(boardTileSideLength*13, boardTileSideLength, 4, boardTileSideLength, spacing));
        ships.add(new Ship(boardTileSideLength*17+spacing, boardTileSideLength, 2, boardTileSideLength,spacing));
        ships.add(new Ship(boardTileSideLength*13, 2*boardTileSideLength+spacing, 1, boardTileSideLength, spacing));
        ships.add(new Ship(boardTileSideLength*14+spacing, 2*boardTileSideLength+spacing, 5, boardTileSideLength, spacing));
        ships.add(new Ship(boardTileSideLength*13, 3*boardTileSideLength+2*spacing, 3, boardTileSideLength, spacing));
        ships.add(new Ship(boardTileSideLength*16+spacing, 3*boardTileSideLength+2*spacing, 3, boardTileSideLength, spacing));
    }





    // handling messages from eventbus
    @Subscribe
    public void handleMessage(EventMessage eventMessage) {

    }
}
