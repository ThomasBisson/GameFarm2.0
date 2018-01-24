package com.bissonthomas.gamefarm.view;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("GAME");
        this.setSize(900,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    public void initFrame() {
        this.setVisible(true);
    }
}
