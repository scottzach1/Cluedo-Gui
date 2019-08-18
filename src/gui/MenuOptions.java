package gui;

import game.CluedoGame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuOptions extends JMenuBar {

	private final CluedoGame cluedoGame;

	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------
	public MenuOptions(CluedoGame aCluedoGame) {
		cluedoGame = aCluedoGame;

		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.width = GUI.SCREEN_WIDTH;
		size.height = GUI.SCREEN_HEIGHT/20;
		setPreferredSize(size);
		setFont(new Font("Serif", Font.BOLD, 18));
		
		// Create the file menu
		JMenu file = new JMenu("File");
		JMenuItem restartItem = new JMenuItem("Restart");
		JMenuItem closeItem = new JMenuItem("Close");
		JMenuItem rulesItem = new JMenuItem("Rules");
		
		// Add buttons to directories
		file.add(restartItem);
		file.add(closeItem);
		file.add(rulesItem);

		file.setPreferredSize(new Dimension(size.width / 20, size.height));
		
		add(file);
		
		
		
		// Button actions:
		// Restart
		restartItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cluedoGame.getGui().restartGame()){
					cluedoGame.restartGame();
				}
			}
		});
		
		// Close
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cluedoGame.getGui().exitGame()){
					System.exit(0);
				}
			}			
		});
		
	}
}
