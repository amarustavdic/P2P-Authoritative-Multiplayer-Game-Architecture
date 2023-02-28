package com.mygame.app.ui;

import com.mygame.app.game.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Droppers extends JPanel {
    private char NEXT_TURN = 'e';
    private PlayerInfo localPlayer;
    private PlayerInfo otherPlayer;
    private GameGrid gameGrid;



    public Droppers(GameGrid gameGrid, PlayerInfo localPlayer, PlayerInfo otherPlayer) {
        this.localPlayer = localPlayer;
        this.otherPlayer = otherPlayer;
        this.gameGrid = gameGrid;
        setBackground(Color.decode("#5E503F"));
        GridLayout gridLayout = new GridLayout(1,7);
        gridLayout.setVgap(0);
        gridLayout.setHgap(0);
        setLayout(gridLayout);

        DroppersAction droppersAction = new DroppersAction();
        RoundButton[] droppers = new RoundButton[7];
        for (int i = 0; i < 7; i++) {
            droppers[i] = new RoundButton(20);
            droppers[i].setBackground(Color.decode("#5E503F"));
            droppers[i].setFocusable(false);
            droppers[i].setIndex(i);
            droppers[i].addMouseListener(droppersAction);
            add(droppers[i]);
        }




    }

    class DroppersAction implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            RoundButton dropper = (RoundButton) e.getSource();
            try {
                if (gameGrid.animateMove(dropper.getIndex(), NEXT_TURN)) {
                    if (GameLogic.hasFourInARow()) System.out.println(NEXT_TURN + " won! 1");
                    if (NEXT_TURN == 'r') NEXT_TURN = 'y';
                    else NEXT_TURN = 'r';
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }




            Timer timer = new Timer(4000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int j = GameLogic.computerMove();
                        if (gameGrid.animateMove(j, NEXT_TURN)) {
                            if (GameLogic.hasFourInARow()) System.out.println(NEXT_TURN + " won! 2");
                            if (NEXT_TURN == 'r') NEXT_TURN = 'y';
                            else NEXT_TURN = 'r';
                            ((Timer)e.getSource()).stop();
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            timer.start();

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
            switch (NEXT_TURN) {
                case 'r': dropper.setBackground(Color.RED); break;
                case 'y': dropper.setBackground(Color.YELLOW); break;
                default:
                    break;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            RoundButton dropper = (RoundButton) e.getSource();
            dropper.setBackground(Color.decode("#5E503F"));
        }
    }


    public void setNEXT_TURN(char NEXT_TURN) {
        this.NEXT_TURN = NEXT_TURN;
    }
}
