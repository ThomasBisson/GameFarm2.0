package com.bissonthomas.gamefarm.controler.states;

import com.bissonthomas.gamefarm.controler.StateManager;
import com.bissonthomas.gamefarm.model.*;
import com.bissonthomas.gamefarm.view.PlayPanel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayState extends State {

    public final int xInventory = 500;
    public final int yInventory = 323;

    public final int xShop = 50;
    public final int yShop = 143;


    private PlayPanel playPanel;

    private Player player;

    private HashMap<String, Integer> hashMap_map_indexmap;
    private ArrayList<Map> map;
    private int xMap=0, yMap=0;

    //TODO lA CLASSE EST TROP LONGUE ET INCOMPREHENSIBLE, ELLE VA SE SEPARER PLUS TARD

    public PlayState(StateManager sm, DBMongoConnection dbmc, String name, BufferedImage icon) {
        super(sm, dbmc);
        playPanel = new PlayPanel();
        //instantiateInitialMap();
        instantiateMaps();
        instantiateInitialPlayer(name, icon);
    }

    /**
     * Instantiate the icon, the name, the purse and the starting seed of the player
     * @param name
     * @param icon
     */
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

    public void instantiateMaps() {
        map = new ArrayList<>();
        hashMap_map_indexmap = new HashMap<>();
        ArrayList<JSONObject> allMaps = dbmc.getColectionIterableMap();
        for(int i=0; i<allMaps.size(); i++) {
            try {
                map.add(new Map(allMaps.get(i).getInt(DBMongoConnection.X_MAP), allMaps.get(i).getInt(DBMongoConnection.Y_MAP),
                        allMaps.get(i).getInt(DBMongoConnection.PRICE_MAP), allMaps.get(i).getBoolean(DBMongoConnection.BOUGHT_AT_START_MAP)));
                hashMap_map_indexmap.put(indiceToString(map.get(i).getIdX(), map.get(i).getIdY()), i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayList<JSONObject> alGround, alFlowers;
        JSONArray jsonArray;
        String[] flowersName;
        for(int i =0; i<allMaps.size(); i++) {
            //get the ground for the map
            alGround = dbmc.getColectionIterableGround(indiceToString(map.get(i).getIdX(), map.get(i).getIdY()));

            //get the flowers for the map
            jsonArray = allMaps.get(i).optJSONArray(DBMongoConnection.FLOWERS_MAP);
            flowersName = new String[jsonArray.length()];
            for(int j=0; j < jsonArray.length(); j++)
                try {
                    flowersName[j] = jsonArray.getString(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            alFlowers = dbmc.getColectionFlowerIterable(false, flowersName);

            //Putting the ground in the map
            if(alGround != null) {
                for (int j = 0; j < alGround.size(); j++) {
                    map.get(i).addGround(Ground.jsonToGround(alGround.get(j), j));
                    map.get(i).getALGround().get(j).addObserver(playPanel);
                }
            }

            //Putting the flowers in the map
            if(alFlowers != null) {
                for (int j = 0; j < alFlowers.size(); j++)
                    map.get(i).addPlant(Plant.jsonToPlant(alFlowers.get(j)));
            }

        }
        buyAMap(xMap,yMap);
        activateGrounds(xMap, yMap);
        changeMap();
    }

    public void changeMap() {
        try {
            playPanel.setCurrentBackground(ImageIO.read(new File("maps/" + indiceToString() + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create the images of the actual map and set a mouse listener to each of them
        playPanel.createGrounds(map.get(hashMap_map_indexmap.get(indiceToString())));
        activateGrounds(xMap, yMap);
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
                playPanel.displayShop(xShop, yShop, map.get(hashMap_map_indexmap.get(indiceToString())).getPlantInShop(), player);
                initShop();
            }
        });

        playPanel.getlArrowEast().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (! isMapAlreadyBought(xMap + 1, yMap)) {
                    //TODO changer ça, faut utiliser une map de l'array list, plus un json object
                    JSONObject mapTemp = dbmc.getColectionIterableMap(xMap + 1, yMap);
                    boolean isOK = displayMessageBoxBuyAMap(mapTemp);
                    if(isOK){
                        buyAMap(xMap+1, yMap);
                        desactivateGrounds(xMap, yMap);
                        xMap++;
                        changeMap();
                    }
                } else {
                    desactivateGrounds(xMap, yMap);
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
                        desactivateGrounds(xMap, yMap);
                        xMap--;
                        changeMap();
                    }
                } else {
                    desactivateGrounds(xMap, yMap);
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
                        desactivateGrounds(xMap, yMap);
                        yMap--;
                        changeMap();
                    }
                } else {
                    desactivateGrounds(xMap, yMap);
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
                        desactivateGrounds(xMap, yMap);
                        yMap++;
                        changeMap();
                    }
                } else {
                    desactivateGrounds(xMap, yMap);
                    yMap++;
                    changeMap();
                }
            }
        });
    }

    public boolean displayMessageBoxBuyAMap(JSONObject mapTemp) {
        int option = 0;
        try {
            option = JOptionPane.showConfirmDialog(null,

                    "Price : " + mapTemp.getInt(DBMongoConnection.PRICE_MAP) +
                            "g\nDo you want to buy this map ?", "",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                if (player.getPurse() >= mapTemp.getInt(DBMongoConnection.PRICE_MAP)) {
                    player.setPurse(player.getPurse() - mapTemp.getInt(DBMongoConnection.PRICE_MAP));
                    return true;
                } else
                    JOptionPane.showMessageDialog(null, "You don't have enought money !", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
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
                if(map.get(hashMap_map_indexmap.get(indiceToString())).getALGround().get(indiceGround).getPlant() != null) {
                    if(map.get(hashMap_map_indexmap.get(indiceToString())).getALGround().get(indiceGround).getPlant().getState() == Plant.PlantState.BIGPLANT) {
                        Plant p = map.get(hashMap_map_indexmap.get(indiceToString())).getALGround().get(indiceGround).harvestPlant();
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
        playPanel.getInventoryPanel().getbClose().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                playPanel.remove(playPanel.getInventoryPanel());
                playPanel.setInventoryPanel(null);
                playPanel.refresh();
            }
        });
        for(int y = 0; y<playPanel.getInventoryPanel().getArrayImagePlants().length; y++) {
            for(int x = 0; x<playPanel.getInventoryPanel().getArrayImagePlants()[0].length; x++) {
                final int yy = y; final int xx = x;
                if(playPanel.getInventoryPanel().getArrayImagePlants()[y][x].getIcon() != null) {
                    playPanel.getInventoryPanel().getArrayImagePlants()[y][x].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) { }
                        @Override
                        public void mousePressed(MouseEvent e) { }
                        @Override
                        public void mouseReleased(MouseEvent e) { }
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            playPanel.getInventoryPanel().getArrayImagePlants()[yy][xx].setIcon(
                                    new ImageIcon("imagesPlantes/" +
                                            player.getBag().getArrayPlant()[yy * 5 + xx][0].getName() +
                                            "Selected.png"));
                        }
                        @Override
                        public void mouseExited(MouseEvent e) {
                            playPanel.getInventoryPanel().getArrayImagePlants()[yy][xx].setIcon(
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
        playPanel.getInventoryPanel().getbClose().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                playPanel.remove(playPanel.getInventoryPanel());
                if(playPanel.getGroundChoicePanel() != null)
                    playPanel.remove(playPanel.getGroundChoicePanel());
                playPanel.setInventoryPanel(null);
                playPanel.setGroundChoicePanel(null);
                playPanel.refresh();
            }
        });
        for(int y = 0; y<playPanel.getInventoryPanel().getArrayImagePlants().length; y++) {
            for(int x = 0; x<playPanel.getInventoryPanel().getArrayImagePlants()[0].length; x++) {
                final int yy = y; final int xx = x;
                if(playPanel.getInventoryPanel().getArrayImagePlants()[y][x].getIcon() != null) {
                    playPanel.getInventoryPanel().getArrayImagePlants()[y][x].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) { }
                        @Override
                        public void mousePressed(MouseEvent e) { }
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            //mettre une plante dans le ground et la retirer du sac
                            if(map.get(hashMap_map_indexmap.get(indiceToString())).getALGround().get(indiceGround).getPlant() == null)
                                map.get(hashMap_map_indexmap.get(indiceToString())).getALGround().get(indiceGround).addPlant(player.getBag().getPlantAndRemoveIt(player.getBag().plantNameInStack(yy * 5 + xx)));
                            playPanel.remove(playPanel.getInventoryPanel());
                            playPanel.remove(playPanel.getGroundChoicePanel());
                            playPanel.setInventoryPanel(null);
                            playPanel.setGroundChoicePanel(null);
                            playPanel.refresh();
                        }
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            playPanel.getInventoryPanel().getArrayImagePlants()[yy][xx].setIcon(
                                    new ImageIcon("imagesPlantes/" +
                                            player.getBag().getArrayPlant()[yy * 5 + xx][0].getName() +
                                            "Selected.png"));
                        }
                        @Override
                        public void mouseExited(MouseEvent e) {
                            playPanel.getInventoryPanel().getArrayImagePlants()[yy][xx].setIcon(
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

    public void initShop(){
        playPanel.getShopPanel().getbClose().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPanel.remove(playPanel.getShopPanel());
                playPanel.setShopPanel(null);
                playPanel.refresh();
            }
        });
        for(int i=0; i<playPanel.getShopPanel().getShopCasesBuy().size(); i++) {
            final int ii = i;
            playPanel.getShopPanel().getShopCasesBuy().get(i).getB_buy().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (player.getPurse() >= playPanel.getShopPanel().getShopCasesBuy().get(ii).getPlant().getPrice()) {
                        player.setPurse(player.getPurse() - playPanel.getShopPanel().getShopCasesBuy().get(ii).getPlant().getPrice());
                        player.getBag().addPlant(playPanel.getShopPanel().getShopCasesBuy().get(ii).getPlant());
                    }
                }
            });
        }
        for(int i=0; i<playPanel.getShopPanel().getShopCasesSell().size(); i++) {
            final int ii = i;
            playPanel.getShopPanel().getShopCasesSell().get(i).getB_buy().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player.setPurse(player.getPurse() + playPanel.getShopPanel().getShopCasesSell().get(ii).getPrice());
                    player.getBag().removePlant(playPanel.getShopPanel().getShopCasesSell().get(ii).getPlant().getName());
                    //playPanel.getShopPanel().instantiateShopCasesSell(player);
                    //playPanel.getShopPanel().refresh();
                }
            });
        }
    }

    public String indiceToString() { return xMap + "," + yMap; }
    public String indiceToString(int x, int y) { return x + "," + y; }

    public boolean isMapAlreadyBought(int x, int y) {
        return map.get(hashMap_map_indexmap.get(indiceToString(x,y))).isAlreadyBought();
    }
    public void buyAMap(int x, int y) {
        map.get(hashMap_map_indexmap.get(indiceToString(x,y))).setAlreadyBought(true);
    }
    public void activateGrounds(int x, int y) {
        for(int i=0; i<map.get(hashMap_map_indexmap.get(indiceToString(x,y))).getALGround().size(); i++) {
            map.get(hashMap_map_indexmap.get(indiceToString(x, y))).getALGround().get(i).setDisplayed(true);
            map.get(hashMap_map_indexmap.get(indiceToString(x, y))).getALGround().get(i).notifyTheObservers();
        }
    }
    public void desactivateGrounds(int x, int y) {
        for(int i=0; i<map.get(hashMap_map_indexmap.get(indiceToString(x,y))).getALGround().size(); i++)
            map.get(hashMap_map_indexmap.get(indiceToString(x,y))).getALGround().get(i).setDisplayed(false);
    }

    @Override
    public JLayeredPane getPanel() {
        return playPanel;
    }
}
