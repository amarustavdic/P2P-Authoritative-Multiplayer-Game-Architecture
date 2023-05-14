package com.mygame.app.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mygame.app.ui.ebus.EventMessage;
import com.mygame.app.ui.ebus.EventType;

import javax.swing.*;
import java.awt.*;


public class GameUI extends JFrame {

    private final EventBus eventBus;
    private JComponent currentPanel;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;



    public GameUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        this.eventBus = new EventBus();
        eventBus.register(this);

        MainView mainView = new MainView(WIDTH, HEIGHT, eventBus);


        currentPanel = mainView;
        this.setContentPane(currentPanel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    @Subscribe
    public void onPanelChange(EventMessage eventMessage) {
        if (eventMessage.getEventType().equals(EventType.SINGLE_PLAYER_BTN)) {
            // here change the board you want to present


            Dimension d = new Dimension(getWidth(), getHeight());
            JPanel panel = new JPanel();
            panel.setSize(d);
            panel.setPreferredSize(d);
            panel.setLayout(null);

            // adding other components here temporary

            GridComponent gridComponent = new GridComponent(10, 120, 40);
            panel.add(gridComponent);

            ShipComponent[] shipComponents = new ShipComponent[10];
            shipComponents[0] = new ShipComponent(600, 200, 4);
            shipComponents[1] = new ShipComponent(500, 245, 3);
            shipComponents[2] = new ShipComponent(600, 200, 3);
            shipComponents[3] = new ShipComponent(600, 200, 2);
            shipComponents[4] = new ShipComponent(600, 200, 2);
            shipComponents[5] = new ShipComponent(600, 200, 2);
            shipComponents[6] = new ShipComponent(600, 200, 1);
            shipComponents[7] = new ShipComponent(600, 200, 1);
            shipComponents[8] = new ShipComponent(600, 200, 1);
            shipComponents[9] = new ShipComponent(600, 200, 1);

            shipComponents[3].setHitVector(new boolean[] {true, false});
            shipComponents[7].setHitVector(new boolean[] {true});
            shipComponents[1].setHitVector(new boolean[] {true, false, true});


            for (ShipComponent shipComponent : shipComponents) {
                panel.add(shipComponent);
            }





            currentPanel = panel;
            this.setContentPane(currentPanel);

        }
        revalidate();
        repaint();
    }
}
