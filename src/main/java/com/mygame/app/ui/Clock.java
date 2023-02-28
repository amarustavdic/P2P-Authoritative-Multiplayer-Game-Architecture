package com.mygame.app.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Clock extends JPanel {

    public Clock() {
        setBackground(new Color(110,103,95));
        JLabel time = new JLabel("10:00");
        time.setForeground(new Color(227,178,60));
        add(time);
        setBorder(new LineBorder(Color.BLACK, 1));



    }



}
