package com.bissonthomas.gamefarm.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Plant {

    public enum PlantState {
        SEED,
        LITTLEPLANT,
        BIGPLANT,
        PLANT;
    }

    private String name;
    private String description;
    private PlantState state;
    private int growTime; //in ms
    private int price;
    private int nbHarvest;

    public Plant(String name, String description, int growTime, int price, int nbHarvest) {
        this.name = name;
        this.description = description;
        this.growTime = growTime;
        this.price = price;
        this.nbHarvest = nbHarvest;
        state = null;
    }

    public static Plant jsonToPlant(JSONObject json) {
        try {
            String name = (String) json.get(DBMongoConnection.NAME_FLOWERS);
            String description = (String) json.get(DBMongoConnection.DESCRIPTION_FLOWERS);
            int growtime = (int) json.get(DBMongoConnection.GROWTIME_FLOWERS);
            int price = (int) json.get(DBMongoConnection.PRICE_FLOWERS);
            int nbHarvest = (int) json.get(DBMongoConnection.HARVEST_FLOWERS);
            return new Plant(name, description, growtime, price, nbHarvest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Plant clonePlant(Plant p) {
        return new Plant(p.name, p.description, p.growTime, p.price, p.nbHarvest);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public PlantState getState() {
        return state;
    }
    public void setState(PlantState state) {
        this.state = state;
    }

    public int getGrowTime() {
        return growTime;
    }
    public void setGrowTime(int growTime) {
        this.growTime = growTime;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getNbHarvest() { return nbHarvest; }
}
