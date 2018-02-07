package com.bissonthomas.gamefarm.view.playgame;

import com.bissonthomas.gamefarm.model.Plant;
import com.bissonthomas.gamefarm.model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShopPanel extends JPanel {

    public final int HeightTitle = 30;
    public final int HeightJPanel = 400;
    public final int WidthJPanel = 400;

    private JButton bClose;
    private ArrayList<ShopCasePanel> shopCasesBuy, shopCasesSell;

    private JScrollPane scrollBuy, scrollSell;
    private JPanel pItems;
    private JTabbedPane tabbed;

    public ShopPanel(int x, int y, ArrayList<Plant> plants, Player player) {

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



        pItems = new JPanel();
        pItems.setBounds(0,HeightTitle,HeightJPanel, WidthJPanel);
        tabbed = new JTabbedPane();
        tabbed.setBounds(-10,-10,HeightJPanel, WidthJPanel);
        tabbed.add("buy", instantiateShopCasesBuy(plants) );
        tabbed.add("sell", instantiateShopCasesSell(player));
        pItems.add(tabbed);


        this.add(pTitle);
        this.add(pItems);
        pTitle.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.BLACK));
        pItems.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setVisible(true);

    }

    public JScrollPane instantiateShopCasesBuy(ArrayList<Plant> plants) {
        JPanel pBuy = new JPanel();
        pBuy.setBounds(0,HeightTitle, WidthJPanel, HeightJPanel);
        pBuy.setLayout(new GridLayout(plants.size(),1));
        scrollBuy = new JScrollPane();
        scrollBuy.setViewportView(pBuy);
        shopCasesBuy = new ArrayList<>();

        for(int i=0; i<plants.size(); i++) {
            shopCasesBuy.add(new ShopCasePanelBuy(plants.get(i)));
            pBuy.add(shopCasesBuy.get(i));
        }

        return scrollBuy;
    }

    public JScrollPane instantiateShopCasesSell(Player player) {
        JPanel pSell = new JPanel();
        pSell.setBounds(0,HeightTitle, WidthJPanel, HeightJPanel);
        pSell.setLayout(new GridLayout(player.getBag().howManyStacksNoEmptyStacks(),1));
        scrollSell = new JScrollPane();
        scrollSell.setViewportView(pSell);
        shopCasesSell = new ArrayList<>();

        for(int i=0; i<player.getBag().getArrayPlant().length; i++) {
            if(! player.getBag().isStackEmpty(i)) {
                shopCasesSell.add(new ShopCasePanelSell(player.getBag().getArrayPlant()[i][0], player.getBag().howMuchInStack(i) ));
                pSell.add(shopCasesSell.get(i));
            }
        }
        return scrollSell;
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    public JButton getbClose() { return bClose; }
    public ArrayList<ShopCasePanel> getShopCasesBuy() { return shopCasesBuy; }
    public ArrayList<ShopCasePanel> getShopCasesSell() { return shopCasesSell; }



    public class ShopCasePanel extends JPanel {

        protected Plant plant;
        protected JLabel l_icon;
        protected JLabel l_text;
        protected JLabel l_price;
        protected JButton b_buy;
        protected int price;

        public ShopCasePanel(Plant plant ) {
            this.plant = plant;
            setSize(WidthJPanel, 55);
        }

        public JLabel getL_icon() { return l_icon; }
        public void setL_icon(JLabel l_icon) { this.l_icon = l_icon; }
        public JLabel getL_text() { return l_text; }
        public void setL_text(JLabel l_text) { this.l_text = l_text; }
        public JButton getB_buy() { return b_buy; }
        public void setB_buy(JButton b_buy) { this.b_buy = b_buy; }
        public JLabel getL_price() { return l_price; }
        public void setL_price(JLabel l_price) { this.l_price = l_price; }
        public Plant getPlant() { return plant; }
        public void setPlant(Plant plant) { this.plant = plant; }
        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
    }

    public class ShopCasePanelBuy extends ShopCasePanel {
        public ShopCasePanelBuy(Plant plant) {
            super(plant);
            this.setLayout(new GridLayout(1,4));

            l_icon = new JLabel();
            l_icon.setIcon(new ImageIcon("imagesPlantes/" + plant.getName() + ".png"));
            l_text = new JLabel(plant.getName());
            l_price = new JLabel(plant.getPrice()+"");
            b_buy = new JButton("BUY");
            add(l_icon);
            add(l_text);
            add(l_price);
            add(b_buy);
            this.setVisible(true);
        }
    }

    public class ShopCasePanelSell extends ShopCasePanel {

        private int divisor = 2;

        private JLabel l_number;
        private int number;

        public ShopCasePanelSell(Plant plant, int number) {
            super(plant);
            this.number = number;

            this.setLayout(new GridLayout(1,5));

            l_icon = new JLabel();
            l_icon.setIcon(new ImageIcon("imagesPlantes/" + plant.getName() + ".png"));
            l_text = new JLabel(plant.getName());
            l_number = new JLabel(""+this.number);
            price = plant.getPrice()/divisor;
            l_price = new JLabel(price+"");
            b_buy = new JButton("SELL");
            add(l_icon);
            add(l_text);
            add(l_number);
            add(l_price);
            add(b_buy);
            this.setVisible(true);
        }
    }
}
