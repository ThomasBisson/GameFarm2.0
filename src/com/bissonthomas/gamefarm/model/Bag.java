package com.bissonthomas.gamefarm.model;

import java.util.HashMap;

public class Bag {

    private Plant[][] listPlant;

    public Bag(int nbX, int nby) {
        listPlant = new Plant[20][99];
    }

    //TODO proposer un int pour dire combien de plantes on veut mettre dans le sac
    public boolean addPlant(Plant t) {
        for(int i=0; i<listPlant.length; i++) {
            if(listPlant[i][0] == null) {
                listPlant[i][0] = t;
                return true;
            } else {
                if (listPlant[i][0].getName().equals(t.getName())) {
                    for(int j=0; j<listPlant[0].length; j++) {
                        if(listPlant[i][j] == null) {
                            listPlant[i][j] = t;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean removePlant(String plantName) {
        for(int i=0; i<listPlant.length;i++) {
            if(listPlant[i][0].getName().equals(plantName)) {
                for (int j = 0; j < listPlant[0].length; j++) {
                    if(listPlant[i][j] == null) {
                        listPlant[i][j - 1] = null;
                        putBadInOrder();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Plant getPlantAndRemoveIt(String plantName) {
        for(int i=0; i<listPlant.length;i++) {
            if(listPlant[i][0].getName().equals(plantName)) {
                for (int j = 0; j < listPlant[0].length; j++) {
                    if(listPlant[i][j] == null) {
                        Plant p = listPlant[i][j - 1];
                        listPlant[i][j - 1] = null;
                        putBadInOrder();
                        return p;
                    }
                }
            }
        }
        return null;
    }

    private void putBadInOrder() {
        for(int i=0; i<listPlant.length;i++) {
            if(listPlant[i][0] == null) {
                //Si on est pas a la fin du sac
                if(i<listPlant.length-1) {
                    for(int j=i+1; j<listPlant.length;j++) {
                        if(listPlant[j][0] != null) {
                            for(int k=0; k<listPlant[0].length;k++) {
                                listPlant[i][k] = listPlant[j][k];
                                listPlant[j][k] = null;
                            }
                            putBadInOrder();
                            return;
                        }
                    }
                }
            }
        }
    }

    public boolean isStackEmpty(int y){
        return listPlant[y][0] == null;
    }

    public int howMuchInStack(int y){
        for(int i=0; i<listPlant[0].length ; i++){
            if(listPlant[y][i] == null)
                return i;
        }
        return 99;
    }

    public String plantNameInStack(int stack) { return listPlant[stack][0].getName(); }

    public Plant[][] getArrayPlant(){
        return listPlant;
    }
    public void setArrayPlant(Plant[][] listPlant){
        this.listPlant = listPlant;
    }

}
