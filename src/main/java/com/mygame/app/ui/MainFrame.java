package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final int WIDTH = 660;
    private final int HEIGHT = 507;
    private final ContainerPanel containerPanel;


    public MainFrame() {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        //setUndecorated(true);
        //setResizable(false);
        setVisible(true);

        containerPanel = new ContainerPanel();

        add(containerPanel, BorderLayout.CENTER);
        pack();
    }


}
