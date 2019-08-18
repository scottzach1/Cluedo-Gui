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
	private static boolean exitRules;
	private static JMenuItem restartItem;
	private static JMenuItem closeItem;
	private static JMenuItem rulesItem;

	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------
	public MenuOptions(CluedoGame aCluedoGame) {
		cluedoGame = aCluedoGame;
		exitRules = false;

		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.width = GUI.SCREEN_WIDTH;
		size.height = GUI.SCREEN_HEIGHT/20;
		setPreferredSize(size);
		setFont(new Font("Serif", Font.BOLD, 18));
		
		// Create the file menu
		JMenu file = new JMenu("File");
		restartItem = new JMenuItem("Restart");
		closeItem = new JMenuItem("Close");
		rulesItem = new JMenuItem("Rules");

		
		// Add buttons to directories
		file.add(restartItem);
		file.add(rulesItem);
		file.add(closeItem);

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

		// Rules
		rulesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cluedoGame.getState() != CluedoGame.State.PLAYER_COUNT && cluedoGame.getState() != CluedoGame.State.USER_CREATION) {
					rulesItem.setText((exitRules = !exitRules) ? "Exit Rules" : "Rules");
					if (!exitRules) {
						cluedoGame.gameController();
					} else {
						cluedoGame.getGui().displayRules();
					}
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

	public static void setExitRulesFalse(){
		exitRules = false;
		rulesItem.setText("Rules");
	}
}
