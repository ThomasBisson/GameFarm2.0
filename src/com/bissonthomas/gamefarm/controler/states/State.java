package com.bissonthomas.gamefarm.controler.states;

import com.bissonthomas.gamefarm.controler.StateManager;
import com.bissonthomas.gamefarm.model.DBMongoConnection;

import javax.swing.*;
import java.awt.*;

public abstract class State {

    protected StateManager sm;
    protected JPanel panel;
    protected DBMongoConnection dbmc;

    public State(StateManager sm, DBMongoConnection dbmc) {
        this.sm = sm;
        this.dbmc = dbmc;
    }

    public abstract void init();
    public abstract Component getPanel();
}
