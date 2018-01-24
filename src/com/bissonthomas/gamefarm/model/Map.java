package com.bissonthomas.gamefarm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Map {

    private ArrayList<Ground> alGround;
    private ArrayList<Plant> plantInShop;

    public Map(){
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
}
