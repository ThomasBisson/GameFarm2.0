package com.bissonthomas.gamefarm.view;

import com.bissonthomas.gamefarm.model.*;
import com.bissonthomas.gamefarm.view.hud.HUD;
import com.bissonthomas.gamefarm.view.playgame.GroundChoicePanel;
import com.bissonthomas.gamefarm.view.playgame.InventairePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class PlayPanel extends JLayeredPane implements Observer {

    private ArrayList<BufferedImage> backgrounds;
    private BufferedImage currentBackground;
    private BufferedImage caseBlank, caseSeed, caseLittle, casePlant, caseBig;
    private ImageIcon arrowEast, arrowWest, arrowSouth, arrowNorth;
    private JLabel lArrowEast, lArrowWest, lArrowSouth, lArrowNorth;

    private HUD hud;
    private InventairePanel inventairePanel;
    private GroundChoicePanel groundChoicePanel;

    private ArrayList<JLabel> groundsImage;

    //TODO a la place d'instancier les frames sous forme d'image JLabel, on pourrait réutiliser l'idée des couleur et
    // créer un panel en fond qui dessine automatiquement des carré de bonne taille (a voir si le rgb marche avec les couleurs dans les panels
    public PlayPanel() {
        instantiateImage();
        instantiateComponent();
        instantiateMouseEvent();
        setLayout(null);
    }

    public void instantiateImage() {
        try {
            currentBackground = ImageIO.read(new File("maps/0,0.png"));
            caseBlank = ImageIO.read(new File("imagesPlantes/CaseBlank.png"));
            caseSeed = ImageIO.read(new File("imagesPlantes/CaseSeed.png"));
            caseLittle = ImageIO.read(new File("imagesPlantes/CaseLittle.png"));
            casePlant = ImageIO.read(new File("imagesPlantes/CasePlant.png"));
            caseBig = ImageIO.read(new File("imagesPlantes/CaseBig.png"));
            arrowEast = new ImageIcon(this.getClass().getResource("arrowright.gif"));
            arrowWest = new ImageIcon(this.getClass().getResource("arrowleft.gif"));
            arrowSouth = new ImageIcon(this.getClass().getResource("arrowbottom.gif"));
            arrowNorth = new ImageIcon(this.getClass().getResource("arrowtop.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void instantiateComponent() {
        groundsImage = new ArrayList<>();
        hud = new HUD(this);
        add(hud.getbBag());
        add(hud.getbShop());
        lArrowEast = new JLabel(arrowEast);
        lArrowEast.setBounds(800,350, arrowEast.getIconWidth(),arrowEast.getIconHeight());
        lArrowWest = new JLabel(arrowWest);
        lArrowWest.setBounds(10,350, arrowWest.getIconWidth(),arrowWest.getIconHeight());
        lArrowSouth = new JLabel(arrowSouth);
        lArrowSouth.setBounds(450,500, arrowSouth.getIconWidth(),arrowSouth.getIconHeight());
        lArrowNorth = new JLabel(arrowNorth);
        lArrowNorth.setBounds(450,10, arrowNorth.getIconWidth(),arrowNorth.getIconHeight());
    }

    public void instantiateMouseEvent() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(groundChoicePanel!=null) {
                    remove(groundChoicePanel);
                    groundChoicePanel = null;
                    refresh();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if(e.getX()>800)
                    add(lArrowEast,new Integer(1));
                else {
                    remove(lArrowEast);
                    refresh();
                }
                if(e.getX()<100)
                    add(lArrowWest,new Integer(1));
                else {
                    remove(lArrowWest);
                    refresh();
                }
                if(e.getY()>500 && e.getY()<600)
                    add(lArrowSouth,new Integer(1));
                else {
                    remove(lArrowSouth);
                    refresh();
                }
                if(e.getY()<100)
                    add(lArrowNorth,new Integer(1));
                else {
                    remove(lArrowNorth);
                    refresh();
                }
                e.consume();
            }
        });
    }

    public void createGrounds(Map map) {
        for(int i=0; i<groundsImage.size();i++)
            remove(groundsImage.get(i));
        groundsImage.clear();
        JLabel label;
        for(int i=0; i<map.getALGround().size(); i++) {
            label = new JLabel(new ImageIcon(caseBlank));
            label.setBounds(map.getALGround().get(i).getX(), map.getALGround().get(i).getY(), caseBlank.getWidth(),caseBlank.getHeight());
            groundsImage.add(label);
            add(groundsImage.get(i),new Integer(0));
        }
        refresh();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(currentBackground, 0, 0, this);
        hud.draw(g2d);
    }

    public void displayInventory(Player player, int x, int y) {
        if(inventairePanel!=null)
            remove(inventairePanel);
        inventairePanel = new InventairePanel(player, x,y);
        add(inventairePanel,new Integer(1));
        refresh();
    }

    public void displayGroundChoice(int mousePositionOnFrameX, int mousePositionOnFrameY) {
        if(groundChoicePanel!=null)
            remove(groundChoicePanel);
        groundChoicePanel = new GroundChoicePanel(mousePositionOnFrameX, mousePositionOnFrameY);
        add(groundChoicePanel,new Integer(1));
        refresh();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Player) {
            if(arg instanceof String) {
                hud.setName((String) arg);
            } else if(arg instanceof Integer) {
                hud.setPurse((int) arg);
            } else if(arg instanceof BufferedImage) {
                hud.setIcon((BufferedImage) arg);
            }
        } else if(o instanceof Ground) {
            if(arg instanceof Plant) {
                Ground g = (Ground) o;
                if(g.getPlant() == null)
                    groundsImage.get(g.getIndice()).setIcon(new ImageIcon(caseBlank));
                else {
                    switch (g.getPlant().getState()) {
                        case SEED:
                            groundsImage.get(g.getIndice()).setIcon(new ImageIcon(caseSeed));
                            break;
                        case LITTLEPLANT:
                            groundsImage.get(g.getIndice()).setIcon(new ImageIcon(caseLittle));
                            break;
                        case PLANT:
                            groundsImage.get(g.getIndice()).setIcon(new ImageIcon(casePlant));
                            break;
                        case BIGPLANT:
                            groundsImage.get(g.getIndice()).setIcon(new ImageIcon(caseBig));
                            break;
                    }
                }
            }
        }
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    public HUD getHud() { return hud; }
    public InventairePanel getInventairePanel() { return inventairePanel; }
    public void setInventairePanel(InventairePanel inventairePanel) { this.inventairePanel = inventairePanel; }
    public ArrayList<JLabel> getGroundsImage() { return groundsImage; }
    public GroundChoicePanel getGroundChoicePanel() { return groundChoicePanel; }
    public void setGroundChoicePanel(GroundChoicePanel groundChoicePanel) { this.groundChoicePanel = groundChoicePanel; }
    public BufferedImage getCaseBlank() { return caseBlank; }
    public void setCurrentBackground(BufferedImage currentBackground) { this.currentBackground = currentBackground; }
    public ArrayList<BufferedImage> getBackgrounds() { return backgrounds; }

    public JLabel getlArrowEast() { return lArrowEast; }
    public JLabel getlArrowWest() { return lArrowWest; }
    public JLabel getlArrowSouth() { return lArrowSouth; }
    public JLabel getlArrowNorth() { return lArrowNorth; }
}
