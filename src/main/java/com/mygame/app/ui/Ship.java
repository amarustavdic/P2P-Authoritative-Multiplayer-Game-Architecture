package com.mygame.app.ui;

import java.awt.*;

public class Ship {
    public int x;
    public int y;
    public int width;
    public int height;
    public int length;
    public int tileWidth;
    public int tileHeight;
    public boolean isDragging;

    public Ship(int x, int y, int length) {
        this.x = x;
        this.y = y;
        this.length = length;
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
}
