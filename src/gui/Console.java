package gui;

import game.CluedoGame;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class Console extends JPanel {
	
	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------

	private final Color baseCol = new Color(100, 0, 30);

	private GridBagConstraints gc;
	private Dimension size;
	private final CluedoGame cluedoGame;
	private final ImageIcon[] dice = {
			new ImageIcon("dice1.png"),
			new ImageIcon("dice2.png"),
			new ImageIcon("dice3.png"),
			new ImageIcon("dice4.png"),
			new ImageIcon("dice5.png"),
			new ImageIcon("dice6.png")
	};

	
	
	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------
	
	public Console(CluedoGame parent) {
		cluedoGame = parent;

		// Set the Size of the Control panel
		size = getPreferredSize();
		size.width = (GUI.SCREEN_WIDTH / 3) - (Controls.inset / 2);
		size.height = GUI.CONTROLS_HEIGHT - Controls.inset;
		setPreferredSize(size);

		// Create the boarder
		Border b1 = BorderFactory.createRaisedBevelBorder();
		Border b2 = BorderFactory.createLoweredBevelBorder();
		Border b3 = BorderFactory.createCompoundBorder(b1, b2);
		setBorder(b3);
		setBackground(baseCol);

		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridheight = size.height;
		gc.gridwidth = size.width;
	}

	public void drawDice(int dieOne, int dieTwo){
		JLabel dieOneLabel = new JLabel();
		JLabel dieTwoLabel = new JLabel();

		ImageIcon dieOneIcon = dice[dieOne - 1];
		ImageIcon dieTwoIcon = dice[dieTwo - 1];

		Image diceOneImg = dieOneIcon.getImage();
		Image dieTwoImg = dieTwoIcon.getImage();

	}
	
	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------

	
	
	public void clear() {
		removeAll();
	}

}
