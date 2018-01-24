package com.bissonthomas.gamefarm.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    private JButton bNG       = new JButton("New Game");
    private JButton bContinue = new JButton("Continue");
    private JButton bOption   = new JButton("Options");
    private JButton bExit     = new JButton("Exit");

    public MenuPanel(){
        this.setLayout(null);

        bNG.setBounds(300, 200, 300, 70);
        bContinue.setBounds(300, 300, 300, 70);
        bOption.setBounds(300, 400, 300, 70);
        bExit.setBounds(300, 500, 300, 70);

        this.add(bNG);
        this.add(bContinue);
        this.add(bOption);
        this.add(bExit);
    }

    public JButton getbNG() { return bNG; }
    public void setbNG(JButton bNG) { this.bNG = bNG; }

    public JButton getbContinue() { return bContinue; }
    public void setbContinue(JButton bContinue) { this.bContinue = bContinue; }

    public JButton getbOption() { return bOption; }
    public void setbOption(JButton bOption) { this.bOption = bOption; }

    public JButton getbExit() { return bExit; }
    public void setbExit(JButton bExit) { this.bExit = bExit; }

}
