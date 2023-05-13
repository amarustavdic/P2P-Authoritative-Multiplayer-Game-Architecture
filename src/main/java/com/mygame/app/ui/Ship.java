package com.mygame.app.ui;

import java.awt.*;

public class Ship {
    public final Color shipColor = new Color(131, 131, 131);
    public final Color hitMarkerColor = new Color(80, 80, 80);    // neutral color of hit marker
    private final int boardTileSideLength;
    private final int shipLength;                                         // length is in number of tiles used for ship
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isDragging;



    public Ship(int x, int y, int shipLength, int boardTileSideLength, int boardTileSpacing) {
        this.x = x;
        this.y = y;
        this.boardTileSideLength = boardTileSideLength;
        this.shipLength = shipLength;
        this.width = shipLength * (boardTileSideLength + boardTileSpacing) - boardTileSpacing;
        this.height = boardTileSideLength;
        this.isDragging = false;
    }





    public boolean contains(Point p) {
        return this.contains(p.x, p.y);
    }

    public boolean contains(int x, int y) {
        return this.inside(x, y);
    }

    public boolean inside(int X, int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            return false;
        } else {
            int x = this.x;
            int y = this.y;
            if (X >= x && Y >= y) {
                w += x;
                h += y;
                return (w < x || w > X) && (h < y || h > Y);
            } else {
                return false;
            }
        }
    }

    public void setSize(int width, int height) {
        this.resize(width, height);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setLocation(int x, int y) {
        this.move(x, y);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Color getShipColor() {
        return shipColor;
    }

    public Color getHitMarkerColor() {
        return hitMarkerColor;
    }

    public int getBoardTileSideLength() {
        return boardTileSideLength;
    }

    public int getShipLength() {
        return shipLength;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
