package com.mygame.app.ui;

import com.mygame.app.game.GameLogic;
import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class MenuPanel extends JPanel {

    private final ContainerPanel containerPanel;
    private Image backgroundImage;

    public MenuPanel(ContainerPanel containerPanel) {
        this.containerPanel = containerPanel;

        setBackground(Color.RED);
        setLayout(new GridBagLayout());


        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/menu-bg.jpeg")));
        } catch (IOException e) {
            e.printStackTrace();
        }



        JLabel title = new JLabel("Four in Line");
        title.setPreferredSize(new Dimension(400, 50));
        title.setFont(new Font("Arial",Font.BOLD, 40));
        title.setForeground(new Color(229, 70, 0));


        Font font = new Font("Arial", Font.BOLD, 16);
        Dimension btnSize = new Dimension(220, 50);
        JButton[] buttons = new JButton[3];
        buttons[0] = new JButton("PLAY VS COMPUTER");
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                containerPanel.getCardLayout().show(containerPanel, "game");
                GameLogic.init();
                containerPanel.getGamePanel().setUpOtherPlayer(GameLogic.getP2Color(), "Computer");
                containerPanel.getGamePanel().setUpLocalPlayer(GameLogic.getP1Color(), "Amar Ustavdic");
                containerPanel.getGamePanel().nextToMove(GameLogic.getNextTurn());
                containerPanel.getGamePanel().getChat().setVisible(false);

            }
        });
        buttons[1] = new JButton("PLAY ONLINE");
        buttons[2] = new JButton("EXIT GAME");

        for (JButton btn : buttons) {
            btn.setFont(font);
            btn.setPreferredSize(btnSize);
            btn.setBorder(new LineBorder(Color.BLACK, 2));
            btn.setSelected(false);
            btn.setFocusable(false);
        }



        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(title, gbc);

        gbc.gridy = 1;
        add(buttons[0], gbc);

        gbc.gridy = 2;
        add(buttons[1], gbc);

        gbc.gridy = 3;
        add(buttons[2], gbc);



        //add(showGameBtn);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
