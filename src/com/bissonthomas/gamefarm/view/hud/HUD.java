package com.bissonthomas.gamefarm.view.hud;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private JLayeredPane panel;

    private JButton bBag, bShop;

    private String name;
    private BufferedImage icon;
    private int purse = -200000;

    private int decalagePX = 29;

    public HUD(JLayeredPane panel){
        this.panel = panel;
        bBag = new JButton("Inv");
        bBag.setBounds(600,600, 50,50);
        bShop = new JButton("Shop");
        bShop.setBounds(500,600, 50,50);
    }

    public HUD(JPanel panel, String name, BufferedImage icon, int purse) {
        this.name = name;
        this.icon = icon;
        this.purse = purse;
    }

    //TODO J'ai pris une base de 600 ici alors que c'est 700...
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,600-decalagePX, 900, 100);
        g.setColor(Color.WHITE);
        if(icon != null)
            g.drawImage(icon, 10,615-decalagePX, panel);
        if(name != null)
            g.drawString(name, 100,635);
        if(purse != -200000)
            g.drawString(purse+"", 200, 635);
    }

    public void setName(String name) { this.name = name; }
    public void setIcon(BufferedImage icon) { this.icon = icon; }
    public void setPurse(int purse) { this.purse = purse; }

    public JButton getbBag() {
        return bBag;
    }
    public JButton getbShop() { return bShop; }
}
