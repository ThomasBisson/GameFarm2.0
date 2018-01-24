package com.bissonthomas.gamefarm;

import com.bissonthomas.gamefarm.controler.StateManager;
import com.bissonthomas.gamefarm.model.DBMongoConnection;
import com.bissonthomas.gamefarm.view.MainFrame;

public class Main {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        StateManager stateManager = new StateManager(mainFrame);
        stateManager.setState(stateManager.getMenuState());
    }
}
