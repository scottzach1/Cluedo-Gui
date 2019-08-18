package gui;

import game.CluedoGame;
import game.User;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class UserInterface extends JPanel {
	
	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------
	
	private final Color baseCol = new Color(76, 74, 75);
	
	private String borderTitle = "UI";
	private GridBagConstraints gc;
	private Dimension size;
	private final CluedoGame cluedoGame;
	private final Controls parent;
	
	
	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------
	
	public UserInterface(CluedoGame aCluedoGame, Controls aParent) {
		cluedoGame = aCluedoGame;
		parent = aParent;
		// Set the Size of the Control panel
		setPreferredSize(new Dimension((cluedoGame.getGui().getWidth() * 2 / 3) - 3, (cluedoGame.getGui().getHeight())));
		
		// Create the boarder
		Border b1 = BorderFactory.createRaisedBevelBorder();
		Border b2 = BorderFactory.createLoweredBevelBorder();
		Border b3 = BorderFactory.createCompoundBorder(b1, b2);
		setBorder(b3);
		setBackground(baseCol);

		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
	}

	public void gameMenu(){
		gc = new GridBagConstraints();

		// Set Controls title
		User currentUser = cluedoGame.getCurrentUser();
		parent.setBorderTitle(currentUser.getUserName() + "'s turn");
		parent.setBaseCol(currentUser.getSprite().getSpriteColor());
		parent.setAccentCol(currentUser.getSprite().getOpposingColor());

		// Create all the possible buttons
		JButton showHand = new JButton("Show Hand");
		JButton detectivesNotes = new JButton("Detectives Note");
		JButton suggest = new JButton("Suggest");
		JButton accuse = new JButton("Accuse (Solve)");
		JButton skipTurn = new JButton("Skip Turn");

		// Add button functionality


		// Add the player name
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridy = 0;
		gc.gridwidth = 1;
		gc.insets = new Insets(10,5,10,5);
		gc.weighty = 1;
		gc.weightx = 1;

		gc.gridx = 0;
		add(showHand, gc);

		gc.gridx = 1;
		add(detectivesNotes, gc);

		gc.gridx = 2;
		add(suggest, gc);

		gc.gridx = 3;
		add(accuse, gc);


		gc.insets = new Insets(0,getWidth() * 2 / 4,getHeight() * 2 / 4,getWidth() * 2 / 4);
		gc.weighty = 2;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 4;
		add(skipTurn, gc);
	}
	
	
	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------
	
	public void clear() {
		removeAll();
	}
}
