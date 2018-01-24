package com.bissonthomas.gamefarm.model;

import java.awt.image.BufferedImage;
import java.util.Observable;

public class Player extends Observable {

    private String name;
    private BufferedImage icon;
    private int purse;
    private Bag bag;

    public Player() {
        purse = 200;
        bag = new Bag(20,99);
    }

    public Player(String name,BufferedImage icon){
        this.name = name;
        this.icon = icon;
        purse = 200;
        bag = new Bag(20,99);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(this.name);
    }

    public int getPurse(){
        return purse;
    }
    public void setPurse(int purse){
        this.purse = purse;
        setChanged();
        notifyObservers(this.purse);
    }

    //public Bag getBag(){ return bag; }

    public BufferedImage getIcon() { return icon; }
    public void setIcon(BufferedImage icon) {
        this.icon = icon;
        setChanged();
        notifyObservers(this.icon);
    }

    public Bag getBag() { return bag; }
}
