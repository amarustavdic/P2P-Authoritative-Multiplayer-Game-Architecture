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
    private final int WIDTH = GUIConstants.WINDOW_WIDTH;
    private final int HEIGHT = GUIConstants.WINDOW_HEIGHT;



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
            panel.setBackground(GUIConstants.BACKGROUND_COLOR);
            panel.setSize(d);
            panel.setPreferredSize(d);
            panel.setLayout(null);

            // adding other components here temporary

            GridComponent gridComponent = new GridComponent(10, 120, 40);
            panel.add(gridComponent);

            ShipComponent[] shipComponents = new ShipComponent[10];
            shipComponents[0] = new ShipComponent(10+gridComponent.getWidth()+42*3, 204, 4);
            shipComponents[1] = new ShipComponent(10+gridComponent.getWidth()+42*4, 246, 3);
            shipComponents[2] = new ShipComponent(10+gridComponent.getWidth()+42, 246, 3);
            shipComponents[3] = new ShipComponent(10+gridComponent.getWidth()+42*5, 288, 2);
            shipComponents[4] = new ShipComponent(10+gridComponent.getWidth()+42*3, 288, 2);
            shipComponents[5] = new ShipComponent(10+gridComponent.getWidth()+42, 288, 2);
            shipComponents[6] = new ShipComponent(10+gridComponent.getWidth()+42*6, 330, 1);
            shipComponents[7] = new ShipComponent(10+gridComponent.getWidth()+42*5, 330, 1);
            shipComponents[8] = new ShipComponent(10+gridComponent.getWidth()+42*4, 330, 1);
            shipComponents[9] = new ShipComponent(10+gridComponent.getWidth()+42*3, 330, 1);

            shipComponents[3].setHitVector(new boolean[] {true, false});
            shipComponents[7].setHitVector(new boolean[] {true});
            shipComponents[1].setHitVector(new boolean[] {true, false, true});


            for (ShipComponent shipComponent : shipComponents) {
                panel.add(shipComponent);
            }


            RoundButton autoBtn = new RoundButton("Auto");
            autoBtn.setBounds(472,497,84,84);
            panel.add(autoBtn);

            RoundButton saveLoadBtn = new RoundButton("Load");
            saveLoadBtn.setBounds(472,162,42,42);
            panel.add(saveLoadBtn);

            RoundButton homeBtn = new RoundButton("Home");
            homeBtn.setBounds(730,15,60,60);
            panel.add(homeBtn);

            RoundButton battleBtn = new RoundButton("Battle!");
            battleBtn.setBounds(630,497,150,84);
            panel.add(battleBtn);





            currentPanel = panel;
            this.setContentPane(currentPanel);

        }
        revalidate();
        repaint();
    }
}
