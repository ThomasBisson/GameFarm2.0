package com.bissonthomas.gamefarm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Map {

    private int idX;
    private int idY;
    private boolean isAlreadyBought;
    private int price;
    private ArrayList<Ground> alGround;
    private ArrayList<Plant> plantInShop;

    public Map(int idX, int idY, int price, boolean isAlreadyBought){
        this.isAlreadyBought = isAlreadyBought;
        this.idX = idX;
        this.idY = idY;
        this.price = price;
        alGround = new ArrayList<>();
        plantInShop = new ArrayList<>();
    }

    public void removeAllGroundAndPlant() {
        alGround.clear();
        plantInShop.clear();
    }

    public void addGround(Ground ground) {
        alGround.add(ground);
    }
    public void addPlant(Plant plant) { plantInShop.add(plant); }

    public ArrayList<Ground> getALGround(){
        return alGround;
    }
    public ArrayList<Plant> getPlantInShop() { return plantInShop; }
    public int getIdX() { return idX; }
    public int getIdY() { return idY; }
    public int getPrice() { return price; }
    public boolean isAlreadyBought() { return isAlreadyBought; }

    public void setIdY(int idY) { this.idY = idY; }
    public void setIdX(int idX) { this.idX = idX; }
    public void setPrice(int price) { this.price = price; }
    public void setAlreadyBought(boolean isAlreadyBought) { this.isAlreadyBought = isAlreadyBought; }
}
