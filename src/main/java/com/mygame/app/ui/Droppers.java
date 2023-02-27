package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Droppers extends JPanel {




    public Droppers() {
        setBackground(Color.BLACK);
        GridLayout gridLayout = new GridLayout(1,7);
        gridLayout.setVgap(0);
        gridLayout.setHgap(0);
        setLayout(gridLayout);

        DroppersAction droppersAction = new DroppersAction();
        RoundButton[] droppers = new RoundButton[7];
        for (int i = 0; i < 7; i++) {
            droppers[i] = new RoundButton(20);
            droppers[i].setBackground(Color.BLACK);
            droppers[i].setFocusable(false);
            droppers[i].addMouseListener(droppersAction);
            add(droppers[i]);
        }


    }

    class DroppersAction implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            RoundButton dropper = (RoundButton) e.getSource();
            dropper.setBackground(Color.RED);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            RoundButton dropper = (RoundButton) e.getSource();
            dropper.setBackground(Color.BLACK);
        }
    }



}
