package com.bissonthomas.gamefarm.view.playgame;

import javax.swing.*;
import java.awt.*;

public class GroundChoicePanel extends JPanel {

    public final int HeightPanelGroundChoices = 150;
    public final int WidthPanelGroundChoices = 100;

    private JButton bPlant, bHarvest, bExamine;

    public GroundChoicePanel(int x, int y) {
        this.setLayout(null);

        setBounds(x, y, WidthPanelGroundChoices, HeightPanelGroundChoices);

        JPanel choices = new JPanel();
        choices.setBounds(0,0,100, 30);
        choices.setBackground(Color.LIGHT_GRAY);
        JLabel yourChoices = new JLabel("Your choices :");
        choices.add(yourChoices);

        JPanel buttons = new JPanel();
        buttons.setBounds(0,30,100,120);
        buttons.setLayout(new GridLayout(3,1));
        bPlant   = new JButton("To plant");
        bHarvest = new JButton("To harvest");
        bExamine = new JButton("To examine");
        buttons.add(bPlant);
        buttons.add(bHarvest);
        buttons.add(bExamine);


        choices.setBorder(BorderFactory.createLineBorder(Color.black,2));
        buttons.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, Color.BLACK));

        this.add(choices);
        this.add(buttons);
    }

    public JButton getbPlant() { return bPlant; }
    public JButton getbHarvest() { return bHarvest; }
    public JButton getbExamine() { return bExamine; }
}
