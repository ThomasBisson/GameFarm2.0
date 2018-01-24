package com.bissonthomas.gamefarm.view;

import com.bissonthomas.gamefarm.view.newgame.LabelPersoChoiceIcon;
import com.bissonthomas.gamefarm.view.newgame.PanelPersoChoiceIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGamePanel extends JPanel {

    private JTextField name;

    private JRadioButton rbEasy;
    private JRadioButton rbMedium;
    private JRadioButton rbHard;

    private JButton bValidate;
    private JButton bBack;

    //I made it a private variable cause I need to set the static variable isClicked to false when I go back one frame
    private LabelPersoChoiceIcon lImages[];
    //more or less the same
    private PanelPersoChoiceIcon pImages;

    public NewGamePanel() {

        this.setLayout(null);

        JPanel pName = new JPanel();
        pName.setLayout(null);
        pName.setBounds(300, 100, 300, 80);
        pName.setBorder(BorderFactory.createTitledBorder("Enter your pseudonym"));
        name = new JTextField();
        name.setBounds(30, 30, 240, 35);
        name.setHorizontalAlignment(JTextField.CENTER);
        pName.add(name);

        JPanel pLvl = new JPanel();
        pLvl.setLayout(null);
        pLvl.setBounds(300, 210, 300, 80);
        pLvl.setBorder(BorderFactory.createTitledBorder("Select your level of difficulty"));
        rbEasy   = new JRadioButton();
        rbEasy.setBounds(50, 50, 20, 20);
        rbMedium = new JRadioButton();
        rbMedium.setBounds(150, 50, 20, 20);
        rbHard   = new JRadioButton();
        rbHard.setBounds(250, 50, 20, 20);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbEasy);
        bg.add(rbMedium);
        bg.add(rbHard);
        JLabel lEasy = new JLabel("Easy");
        lEasy.setBounds(42, 20, 50, 30);
        JLabel lMedium = new JLabel("Medium");
        lMedium.setBounds(138, 20, 50, 30);
        JLabel lHard = new JLabel("Hard");
        lHard.setBounds(242, 20, 50, 30);
        pLvl.add(lEasy);
        pLvl.add(lMedium);
        pLvl.add(lHard);
        pLvl.add(rbEasy);
        pLvl.add(rbMedium);
        pLvl.add(rbHard);

        pImages = new PanelPersoChoiceIcon();
        pImages.setLayout(new GridLayout());
        pImages.setBounds(150, 320, 600, 200);
        pImages.setBorder(BorderFactory.createTitledBorder("Select your icon"));
        lImages = new LabelPersoChoiceIcon[5];
        for(int i=0; i<lImages.length; i++){
            lImages[i] = new LabelPersoChoiceIcon(pImages,i);
            lImages[i].setIcon(new ImageIcon("icons/" + i + ".png"));
            lImages[i].addMouseListener(lImages[i]);
            pImages.add(lImages[i],BorderLayout.CENTER);
        }

        JPanel pButtons = new JPanel();
        pButtons.setBackground(Color.WHITE);
        pButtons.setLayout(null);
        pButtons.setBounds(0, 580, 900, 120);
        bValidate = new JButton("Validate");
        bValidate.setBounds(150, 30, 250, 40);


        bBack = new JButton("Back");
        bBack.setBounds(500, 30, 250, 40);

        pButtons.add(bValidate);
        pButtons.add(bBack);


        this.add(pName);
        this.add(pLvl);
        this.add(pImages);
        this.add(pButtons);
    }

    public JButton getbValidate() { return bValidate; }
    public JButton getbBack() { return bBack; }

    public JTextField getNameLabel() { return name; }
    public JRadioButton getRbEasy() { return rbEasy; }
    public JRadioButton getRbMedium() { return rbMedium; }

    public JRadioButton getRbHard() {
        return rbHard;
    }

    public LabelPersoChoiceIcon[] getlImages() {
        return lImages;
    }

    public PanelPersoChoiceIcon getpImages() {
        return pImages;
    }

}
