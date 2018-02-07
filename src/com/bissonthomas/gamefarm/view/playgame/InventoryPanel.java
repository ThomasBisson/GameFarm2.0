package com.bissonthomas.gamefarm.view.playgame;

import com.bissonthomas.gamefarm.model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InventoryPanel extends JPanel {

    public final int HeightViewPanelInventory = 220;
    public final int WidthViewPanelInventory = 275;
    public final int HeightViewPanelInventoryTitle = 30;

    private Player player;

    private JLabel arrayImagePlants[][];

    private JButton bClose;


    public InventoryPanel(Player player, int x, int y) {
        this.player = player;

        this.setLayout(null);

        setBounds(x,y, WidthViewPanelInventory, HeightViewPanelInventory + HeightViewPanelInventoryTitle);

        JPanel pTitle = new JPanel();
        pTitle.setLayout(null);
        pTitle.setBounds(0, 0, WidthViewPanelInventory, HeightViewPanelInventoryTitle);
        pTitle.setBackground(Color.LIGHT_GRAY);
        JLabel lInventory = new JLabel("Inventory :");
        lInventory.setBounds(10, 0, 120, 30);
        try {
            BufferedImage buttonIcon = ImageIO.read(new File("icons/redbutton.jpg"));
            bClose = new JButton(new ImageIcon(buttonIcon));
            bClose.setBorder(BorderFactory.createEmptyBorder());
            bClose.setContentAreaFilled(false);
            bClose.setBounds(WidthViewPanelInventory - HeightViewPanelInventoryTitle, 0, HeightViewPanelInventoryTitle, HeightViewPanelInventoryTitle);
            pTitle.add(bClose);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pTitle.add(lInventory);



        JPanel pInventory = new JPanel();
        pInventory.setBounds(0, HeightViewPanelInventoryTitle, WidthViewPanelInventory, HeightViewPanelInventory);
        pInventory.setLayout(new GridLayout(4, 5));
        arrayImagePlants = new JLabel[4][5];
        JLabel lNumberOfPlantInStack;
        for (int i = 0; i < arrayImagePlants.length; i++) {
            for (int j = 0; j < arrayImagePlants[0].length; j++) {
                arrayImagePlants[i][j] = new JLabel();
                if (! this.player.getBag().isStackEmpty(i * 5 + j)) {
                    //TODO il manque les descriptions
                    arrayImagePlants[i][j].setIcon(new ImageIcon("imagesPlantes/" + this.player.getBag().getArrayPlant()[i * 5 + j][0].getName() + ".png"));
                    pInventory.add(arrayImagePlants[i][j]);
                    lNumberOfPlantInStack = new JLabel("" + this.player.getBag().howMuchInStack(i * 5 + j));
                    lNumberOfPlantInStack.setBounds((55 * j) + 40, HeightViewPanelInventoryTitle + (i * 55) + 40, 15, 15);
                    this.add(lNumberOfPlantInStack);
                } else {
                    pInventory.add(arrayImagePlants[i][j]);
                }
            }
        }

        this.add(pTitle);
        this.add(pInventory);
        pTitle.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.BLACK));
        pInventory.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setVisible(true);
    }

    public JButton getbClose() { return bClose; }
    public JLabel[][] getArrayImagePlants() { return arrayImagePlants; }
}
