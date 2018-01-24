package com.bissonthomas.gamefarm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Ground extends Observable {

    private int x,y,indice;
    private Plant plant;
    Timer timer = new Timer();

    public Ground(int x, int y, int indice) {
        this.x = x;
        this.y = y;
        this.indice = indice;
    }



    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public Plant getPlant() { return plant; }
    public void addPlant(Plant _plant) {
        if(plant==null) {
            this.plant = _plant;
            this.plant.setState(Plant.PlantState.SEED);
            setChanged();
            notifyObservers(plant);
            setTimer();
        }
    }
    public Plant harvestPlant() {
        if(plant!=null) {
            Plant p = plant;
            plant = null;
            setChanged();
            notifyObservers(plant);
            return p;
        }
        return null;
    }
    public void removePlant() {

    }

    public int getIndice() { return indice; }
    public void setIndice(int indice) { this.indice = indice; }

    //TODO Voir si le timer marche et si on peut pas faire mieux
    public void setTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(plant.getState() == Plant.PlantState.SEED) {
                    plant.setState(Plant.PlantState.LITTLEPLANT);
                    setChanged();
                    notifyObservers(plant);
                    setTimer();
                } else if(plant.getState() == Plant.PlantState.LITTLEPLANT) {
                    plant.setState(Plant.PlantState.PLANT);
                    setChanged();
                    notifyObservers(plant);
                    setTimer();
                } else if(plant.getState() == Plant.PlantState.PLANT) {
                    plant.setState(Plant.PlantState.BIGPLANT);
                    setChanged();
                    notifyObservers(plant);
                }
            }
        }, plant.getGrowTime()/4);
    }

    public static Ground jsonToGround(JSONObject json, int indice) {
        try {
            int x = (int) json.get(DBMongoConnection.X_GROUNDS);
            int y = (int) json.get(DBMongoConnection.Y_GROUNDS);
            return new Ground(x,y,indice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
