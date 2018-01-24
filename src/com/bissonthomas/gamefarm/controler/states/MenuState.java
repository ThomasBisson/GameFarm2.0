package com.bissonthomas.gamefarm.controler.states;

import com.bissonthomas.gamefarm.controler.StateManager;
import com.bissonthomas.gamefarm.model.DBMongoConnection;
import com.bissonthomas.gamefarm.view.MenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuState extends State {

    private MenuPanel menuPanel;

    public MenuState(StateManager sm, DBMongoConnection dbmc){
        super(sm, dbmc);
        menuPanel = new MenuPanel();
    }

    @Override
    public void init(){
        menuPanel.getbNG().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                //viewFrame.displayNewGame();
                sm.setState(sm.getNewGameState());
            }
        });
        menuPanel.getbContinue().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){

            }
        });
        menuPanel.getbOption().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){

            }
        });
        menuPanel.getbExit().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                //viewFrame.displayExit();
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return menuPanel;
    }
}
