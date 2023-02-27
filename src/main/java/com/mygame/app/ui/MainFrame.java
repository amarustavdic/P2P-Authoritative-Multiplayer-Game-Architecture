package com.mygame.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {

    private static int WIDTH = 600;
    private static int HEIGHT = 400;
    private static JFrame frame;
    private static JPanel panel1;
    private static JPanel panel2;


    public static void init() {
        frame = new JFrame("Four in Line");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(WIDTH,HEIGHT));

        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JButton button1 = new JButton("Next");
        panel1.add(button1, BorderLayout.CENTER);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(panel2);
                frame.revalidate();
                frame.repaint();
            }
        });

        // Create the second panel with a back button
        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JButton button2 = new JButton("Back");
        panel2.add(button2, BorderLayout.CENTER);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(panel1);
                frame.revalidate();
                frame.repaint();
            }
        });

        // Set the initial content pane to the first panel
        frame.setContentPane(panel1);





        frame.setVisible(true);
        frame.pack();
    }



}
