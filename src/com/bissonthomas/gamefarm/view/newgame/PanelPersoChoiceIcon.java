package com.bissonthomas.gamefarm.view.newgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanelPersoChoiceIcon extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	private int arrayCoordRedSquare[][] = {{5,69},{121,69},{239,69},{357,69},{473,69}};
	
	private boolean mustDisplayRedSquare = false;
	
	private int coordX, coordY;
	
	public PanelPersoChoiceIcon(){
		try { 
			image = ImageIO.read(new File("icons/RedSquare.png"));
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void setMustDisplayRedSquare(boolean bool){
		mustDisplayRedSquare = bool;
	}
	
	public int whichIconIsSelected(){
		switch(coordX){
			case 5 :
				return 0;
			case 121 :
				return 1;
			case 239 :
				return 2;
			case 357 :
				return 3;
			case 473 :
				return 4;
			default :
				return 0;
		}
	}
	
	public void setCoordRedSquare(int i){
		coordX = arrayCoordRedSquare[i][0];
		coordY = arrayCoordRedSquare[i][1];
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D)g;
		
		if(mustDisplayRedSquare){
			g2d.drawImage(image, coordX, coordY, this);
		}
	}

	public void repaintPerso(){
		this.repaint();
	}
}
