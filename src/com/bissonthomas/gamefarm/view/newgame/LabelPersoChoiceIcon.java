package com.bissonthomas.gamefarm.view.newgame;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LabelPersoChoiceIcon extends JLabel implements MouseListener{

	private static final long serialVersionUID = 3620629382688440846L;

	private PanelPersoChoiceIcon panelPerso;
	
	private int id;
	
	private static boolean isClicked = false;
	
	public LabelPersoChoiceIcon(PanelPersoChoiceIcon panelPerso, int id){
		this.panelPerso = panelPerso;
		this.id = id;
	}
	
	public void setIsClicked(boolean bool){
		isClicked = bool;
	}
	
	//M�thode appel�e lors du clic de souris
	public void mouseClicked(MouseEvent event) {
		isClicked = true;
		panelPerso.setMustDisplayRedSquare(false);
		panelPerso.setMustDisplayRedSquare(true);
		panelPerso.setCoordRedSquare(id);
		panelPerso.repaintPerso();		
	}

	//M�thode appel�e lors du survol de la souris
	public void mouseEntered(MouseEvent event) {
		if(!isClicked){
			panelPerso.setMustDisplayRedSquare(true);
			panelPerso.setCoordRedSquare(id);
			panelPerso.repaintPerso();
		}
	}

	//M�thode appel�e lorsque la souris sort de la zone du bouton
	public void mouseExited(MouseEvent event) {
		if(!isClicked){
			panelPerso.setMustDisplayRedSquare(false);
			panelPerso.repaintPerso();
		}
	}
	
	//M�thode appel�e lorsque l'on presse le bouton gauche de la souris
	public void mousePressed(MouseEvent event) { }

	//M�thode appel�e lorsque l'on rel�che le clic de souris
	public void mouseReleased(MouseEvent event) { }    
	
}
