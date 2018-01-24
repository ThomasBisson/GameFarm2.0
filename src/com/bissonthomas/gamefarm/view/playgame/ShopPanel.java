package com.bissonthomas.gamefarm.view.playgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShopPanel extends JPanel {

    public final int HeightTitle = 30;
    public final int HeightJPanel = 400;
    public final int WidthJPanel = 400;

    private JButton bClose;

    public ShopPanel(int x, int y) {
        setLayout(null);

        setBounds(x, y, WidthJPanel, HeightJPanel + HeightTitle);

        JPanel pTitle = new JPanel();
        pTitle.setLayout(null);
        pTitle.setBounds(0, 0, WidthJPanel, HeightTitle);
        pTitle.setBackground(Color.LIGHT_GRAY);
        JLabel lInventory = new JLabel("Shop :");
        lInventory.setBounds(10, 0, 120, 30);
        try {
            BufferedImage buttonIcon = ImageIO.read(new File("icons/redbutton.jpg"));
            bClose = new JButton(new ImageIcon(buttonIcon));
            bClose.setBorder(BorderFactory.createEmptyBorder());
            bClose.setContentAreaFilled(false);
            bClose.setBounds(WidthJPanel - HeightTitle, 0, HeightTitle, HeightTitle);
            pTitle.add(bClose);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pTitle.add(lInventory);


    }


    public class ShopCasePanel extends JPanel {
       //public ShopCasePanel()
    }
}
