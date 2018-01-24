package com.bissonthomas.gamefarm.controler.states;

import com.bissonthomas.gamefarm.controler.StateManager;
import com.bissonthomas.gamefarm.model.*;
import com.bissonthomas.gamefarm.view.PlayPanel;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayState extends State {

    public final int xInventory = 500;
    public final int yInventory = 350;

    public final int xShop = 50;


    private PlayPanel playPanel;

    private Player player;

    private Map map;
    private int xMap=0, yMap=0;

    private ArrayList<String> isMapAlreadyBought;

    public PlayState(StateManager sm, DBMongoConnection dbmc, String name, BufferedImage icon) {
        super(sm, dbmc);
        playPanel = new PlayPanel();
        instantiateInitialMap();
        instantiateInitialPlayer(name, icon);
    }

    public void instantiateInitialPlayer(String name, BufferedImage icon){
        player = new Player();
        player.addObserver(playPanel);
        player.setIcon(icon);
        player.setName(name);
        player.setPurse(player.getPurse());
        ArrayList<JSONObject> startingPlants = dbmc.getColectionFlowerIterable(true, null);
        for(int i=0; i<startingPlants.size(); i++)
            player.getBag().addPlant(Plant.jsonToPlant(startingPlants.get(i)));
    }

    public void instantiateInitialMap() {
        isMapAlreadyBought = new ArrayList<>();
        ArrayList<JSONObject> allMaps = dbmc.getColectionIterableMap();
        for(int i=0; i<allMaps.size(); i++)
            isMapAlreadyBought.add(mapToString(allMaps.get(i)));
        buyAMap(0,0);

        map = new Map();
        changeMap();
    }

    public void changeMap() {
        map.removeAllGroundAndPlant();
        ArrayList<JSONObject> alTemp = dbmc.getColectionIterableGround(indiceToString());
        if(alTemp != null) {
            for (int i = 0; i < alTemp.size(); i++) {
                map.addGround(Ground.jsonToGround(alTemp.get(i), i));
                map.getALGround().get(i).addObserver(playPanel);
            }
            alTemp.clear();
        }
        alTemp = dbmc.getColectionFlowerIterable(false, indiceToString());
        if(alTemp != null) {
            for (int i = 0; i < alTemp.size(); i++)
                map.addPlant(Plant.jsonToPlant(alTemp.get(i)));
        }

        try {
            playPanel.setCurrentBackground(ImageIO.read(new File("maps/" + indiceToString() + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playPanel.createGrounds(map);

        for(int i=0; i<playPanel.getGroundsImage().size(); i++){
            final int ii = i;
            playPanel.getGroundsImage().get(i).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) { }
                @Override
                public void mousePressed(MouseEvent e) { }
                @Override
                public void mouseReleased(MouseEvent e) {
                    playPanel.displayGroundChoice(playPanel.getMousePosition().x, playPanel.getMousePosition().y);
                    initGroundChoice(ii);
                }
                @Override
                public void mouseEntered(MouseEvent e) { }
                @Override
                public void mouseExited(MouseEvent e) { }
            });
        }
    }

    @Override
    public void init() {
        playPanel.getHud().getbBag().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPanel.displayInventory(player, xInventory, yInventory );
                initInventory();
            }
        });
        playPanel.getHud().getbShop().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });

        playPanel.getlArrowEast().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (! isMapAlreadyBought(xMap + 1, yMap)) {
                    JSONObject mapTemp = dbmc.getColectionIterableMap(xMap + 1, yMap);
                    boolean isOK = displayMessageBoxBuyAMap(mapTemp);
                    if(isOK){
                        buyAMap(xMap+1, yMap);
                        xMap++;
                        changeMap();
                    }
                } else {
                    xMap++;
                    changeMap();
                }
            }
        });
        playPanel.getlArrowWest().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (! isMapAlreadyBought(xMap - 1, yMap)) {
                    JSONObject mapTemp = dbmc.getColectionIterableMap(xMap - 1, yMap);
                    boolean isOK = displayMessageBoxBuyAMap(mapTemp);
                    if(isOK){
                        buyAMap(xMap-1, yMap);
                        xMap--;
                        changeMap();
                    }
                } else {
                    xMap--;
                    changeMap();
                }
            }
        });
        playPanel.getlArrowSouth().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (! isMapAlreadyBought(xMap, yMap-1)) {
                    JSONObject mapTemp = dbmc.getColectionIterableMap(xMap, yMap-1);
                    boolean isOK = displayMessageBoxBuyAMap(mapTemp);
                    if(isOK){
                        buyAMap(xMap, yMap-1);
                        yMap--;
                        changeMap();
                    }
                } else {
                    yMap--;
                    changeMap();
                }
            }
        });
        playPanel.getlArrowNorth().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (! isMapAlreadyBought(xMap, yMap+1)) {
                    JSONObject mapTemp = dbmc.getColectionIterableMap(xMap, yMap+1);
                    boolean isOK = displayMessageBoxBuyAMap(mapTemp);
                    if(isOK){
                        buyAMap(xMap, yMap+1);
                        yMap++;
                        changeMap();
                    }
                } else {
                    yMap++;
                    changeMap();
                }
            }
        });
    }

    public boolean displayMessageBoxBuyAMap(JSONObject mapTemp) {
        JOptionPane jop = new JOptionPane();
        int option = 0;
        try {
            option = jop.showConfirmDialog(null,
                    "Price : " + mapTemp.getInt(DBMongoConnection.PRICE_MAP) +
                            "g\nDo you want to buy this map ?", "",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (player.getPurse() >= mapTemp.getInt(DBMongoConnection.PRICE_MAP)) {
                    try {
                        player.setPurse(player.getPurse() - mapTemp.getInt(DBMongoConnection.PRICE_MAP));
                    } catch (JSONException e1) { e1.printStackTrace(); }
                    return true;
                } else {
                    JOptionPane jopAlert = new JOptionPane();
                    jopAlert.showMessageDialog(null, "You don't have enought money !", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }



    public void initGroundChoice(int indiceGround) {
        playPanel.getGroundChoicePanel().getbPlant().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPanel.displayInventory(player, xInventory, yInventory );
                initInventoryPlantIntoGround(indiceGround);
            }
        });
        playPanel.getGroundChoicePanel().getbHarvest().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(map.getALGround().get(indiceGround).getPlant() != null) {
                    if(map.getALGround().get(indiceGround).getPlant().getState() == Plant.PlantState.BIGPLANT) {
                        Plant p = map.getALGround().get(indiceGround).harvestPlant();
                        for (int i = 0; i < p.getNbHarvest(); i++) {
                            player.getBag().addPlant(Plant.clonePlant(p));
                        }
                        playPanel.getGroundsImage().get(indiceGround).setIcon(new ImageIcon(playPanel.getCaseBlank()));
                    }
                }
                playPanel.remove(playPanel.getGroundChoicePanel());
                playPanel.setGroundChoicePanel(null);
                playPanel.refresh();
            }
        });
    }

    public void initInventory() {
        playPanel.getInventairePanel().getbClose().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                playPanel.remove(playPanel.getInventairePanel());
                playPanel.setInventairePanel(null);
                playPanel.refresh();
            }
        });
        for(int y=0; y<playPanel.getInventairePanel().getArrayImagePlants().length; y++) {
            for(int x=0; x<playPanel.getInventairePanel().getArrayImagePlants()[0].length; x++) {
                final int yy = y; final int xx = x;
                if(playPanel.getInventairePanel().getArrayImagePlants()[y][x].getIcon() != null) {
                    playPanel.getInventairePanel().getArrayImagePlants()[y][x].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) { }
                        @Override
                        public void mousePressed(MouseEvent e) { }
                        @Override
                        public void mouseReleased(MouseEvent e) { }
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            playPanel.getInventairePanel().getArrayImagePlants()[yy][xx].setIcon(
                                    new ImageIcon("imagesPlantes/" +
                                            player.getBag().getArrayPlant()[yy * 5 + xx][0].getName() +
                                            "Selected.png"));
                        }
                        @Override
                        public void mouseExited(MouseEvent e) {
                            playPanel.getInventairePanel().getArrayImagePlants()[yy][xx].setIcon(
                                    new ImageIcon("imagesPlantes/" +
                                            player.getBag().getArrayPlant()[yy * 5 + xx][0].getName() +
                                            ".png"));
                            //TODO si j'ai l'envi a un moment, mettre le JLabel qui affiche le nombre de plante de le stack
                            playPanel.refresh();
                        }
                    });
                }
            }
        }
    }

    public void initInventoryPlantIntoGround(final int indiceGround) {
        playPanel.getInventairePanel().getbClose().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                playPanel.remove(playPanel.getInventairePanel());
                playPanel.remove(playPanel.getGroundChoicePanel());
                playPanel.setInventairePanel(null);
                playPanel.setGroundChoicePanel(null);
                playPanel.refresh();
            }
        });
        for(int y=0; y<playPanel.getInventairePanel().getArrayImagePlants().length; y++) {
            for(int x=0; x<playPanel.getInventairePanel().getArrayImagePlants()[0].length; x++) {
                final int yy = y; final int xx = x;
                if(playPanel.getInventairePanel().getArrayImagePlants()[y][x].getIcon() != null) {
                    playPanel.getInventairePanel().getArrayImagePlants()[y][x].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) { }
                        @Override
                        public void mousePressed(MouseEvent e) { }
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            //mettre une plante dans le ground et la retirer du sac
                            if(map.getALGround().get(indiceGround).getPlant() == null)
                                map.getALGround().get(indiceGround).addPlant(player.getBag().getPlantAndRemoveIt(player.getBag().plantNameInStack(yy * 5 + xx)));
                            playPanel.remove(playPanel.getInventairePanel());
                            playPanel.remove(playPanel.getGroundChoicePanel());
                            playPanel.setInventairePanel(null);
                            playPanel.setGroundChoicePanel(null);
                            playPanel.refresh();
                        }
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            playPanel.getInventairePanel().getArrayImagePlants()[yy][xx].setIcon(
                                    new ImageIcon("imagesPlantes/" +
                                            player.getBag().getArrayPlant()[yy * 5 + xx][0].getName() +
                                            "Selected.png"));
                        }
                        @Override
                        public void mouseExited(MouseEvent e) {
                            playPanel.getInventairePanel().getArrayImagePlants()[yy][xx].setIcon(
                                    new ImageIcon("imagesPlantes/" +
                                            player.getBag().getArrayPlant()[yy * 5 + xx][0].getName() +
                                            ".png"));
                            //TODO si j'ai l'envi a un moment, mettre le JLabel qui affiche le nombre de plante de le stack
                            playPanel.refresh();
                        }
                    });
                }
            }
        }
    }

    public String indiceToString() { return xMap + "," + yMap; }
    public String mapToString(JSONObject json) {
        try {
            return "false," + json.getInt(DBMongoConnection.X_MAP) +","+json.getInt(DBMongoConnection.Y_MAP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isMapAlreadyBought(int x, int y) {
        for(int i=0; i<isMapAlreadyBought.size();i++) {
            String[] s = isMapAlreadyBought.get(i).split(",");
            if(x==Integer.parseInt(s[1]) && y==Integer.parseInt(s[2]) && Boolean.parseBoolean(s[0])) return true;
        }
        return false;
    }
    public void buyAMap(int x, int y) {
        int indice = 0;
        String[] s = null;
        for(int i=0;i<isMapAlreadyBought.size(); i++) {
            s = isMapAlreadyBought.get(i).split(",");
            if(Integer.parseInt(s[1])==x && Integer.parseInt(s[2]) == y) indice = i;
        }
        s = isMapAlreadyBought.get(indice).split(",");
        isMapAlreadyBought.set(indice,"true," + s[1] + "," + s[2]);
    }

    @Override
    public JLayeredPane getPanel() {
        return playPanel;
    }
}
