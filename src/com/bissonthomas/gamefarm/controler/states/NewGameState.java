package com.bissonthomas.gamefarm.controler.states;

import com.bissonthomas.gamefarm.controler.StateManager;
import com.bissonthomas.gamefarm.model.DBMongoConnection;
import com.bissonthomas.gamefarm.view.NewGamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NewGameState extends State {

    private NewGamePanel newGamePanel;

    public NewGameState(StateManager sm, DBMongoConnection bdmc){
        super(sm, bdmc);
        newGamePanel = new NewGamePanel();
    }


    @Override
    public void init() {
        newGamePanel.getbValidate().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                int icon = newGamePanel.getpImages().whichIconIsSelected();
                if(newGamePanel.getRbEasy().isSelected())   {  }
                if(newGamePanel.getRbMedium().isSelected()) {  }
                if(newGamePanel.getRbHard().isSelected())   {  }
                //viewFrame.getView().getController().getControllerPlayer().createPlayer(newGamePanel.getNameLabel().getText(), icon);
                //viewFrame.displayMainPanel();
                sm.setState(sm.getPlayState(newGamePanel.getNameLabel().getText(), getIconImage(icon)));
            }
        });
        newGamePanel.getbBack().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                newGamePanel.getlImages()[0].setIsClicked(false);
                //viewFrame.displayStartingMenu();
                sm.setState(sm.getMenuState());
            }
        });

    }

    public BufferedImage getIconImage(int icon) {
        try {
            return ImageIO.read(new File("icons/"+icon+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JPanel getPanel() {
        return newGamePanel;
    }
}
