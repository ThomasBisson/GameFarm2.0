package com.bissonthomas.gamefarm.controler;

import com.bissonthomas.gamefarm.controler.states.MenuState;
import com.bissonthomas.gamefarm.controler.states.NewGameState;
import com.bissonthomas.gamefarm.controler.states.PlayState;
import com.bissonthomas.gamefarm.controler.states.State;
import com.bissonthomas.gamefarm.model.DBMongoConnection;
import com.bissonthomas.gamefarm.view.MainFrame;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class StateManager {

    private MainFrame mainFrame;

    private DBMongoConnection mongo;

    private MenuState menuState;
    private NewGameState newGameState;
    private PlayState playState;

    private State currentState;

    public StateManager(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        mongo = new DBMongoConnection();
        if(! mongo.collectionExists(mongo.COLLECTION_FLOWER_NAME))
            mongo.createFlowerColection();
        if(! mongo.collectionExists(mongo.COLLECTION_GROUND_NAME))
            mongo.createGroundCollection();
        if(! mongo.collectionExists(mongo.COLLECTION_MAP_NAME))
            mongo.createMapColection();
    }

    //TODO : a améliorer, le current state sert a rien
    public void setState(State state) {
        if(currentState != null)
            mainFrame.remove(currentState.getPanel());

        if(currentState instanceof MenuState) {
            menuState = null;
        } else if(currentState instanceof NewGameState) {
            newGameState = null;
        } else if(currentState instanceof PlayState) {
            playState = null;
        }

        currentState = state;
        mainFrame.add(currentState.getPanel());
        SwingUtilities.updateComponentTreeUI(mainFrame);

        currentState.init();
        this.mainFrame.initFrame();
    }

    public MenuState getMenuState() {
        if(menuState==null)
            menuState = new MenuState(this, mongo);
        return menuState;
    }

    public NewGameState getNewGameState() {
        if(newGameState==null)
            newGameState = new NewGameState(this, mongo);
        return newGameState;
    }

    public PlayState getPlayState(String name, BufferedImage icon) {
        if(playState==null) {
            playState = new PlayState(this, mongo, name, icon);
        }
        return playState;
    }

    public MainFrame getMainFrame(){
        return mainFrame;
    }
}
