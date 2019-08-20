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

	/**
	 * Constructs a new JMenuBar and sets up the menu options
	 * along with adding action listeners to each button
	 * @param aCluedoGame
	 */
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
		restartItem = new JMenuItem("Restart (q)");
		closeItem = new JMenuItem("Close (w)");
		rulesItem = new JMenuItem("Rules (r)");

		
		// Add buttons to directories
		file.add(restartItem);
		file.add(rulesItem);
		file.add(closeItem);

		file.setPreferredSize(new Dimension(size.width / 20, size.height));
		
		add(file);
		
		
		
		// Button actions:
		// Restart
		restartItem.addActionListener(arg0 -> {
			if (cluedoGame.getGui().restartGame()){
				cluedoGame.restartGame();
			}
		});

		// Rules
		rulesItem.addActionListener(e -> {
		    // As long as you are not setting up the game
			if (cluedoGame.getState() != CluedoGame.State.PLAYER_COUNT && cluedoGame.getState() != CluedoGame.State.USER_NAME_CREATION &&
			cluedoGame.getState() != CluedoGame.State.USER_CHARACTER_SELECTION) {
				rulesItem.setText((exitRules = !exitRules) ? "Exit Rules" : "Rules");
				if (!exitRules) {
					cluedoGame.gameController();
				} else {
					cluedoGame.getGui().displayRules();
				}
			}
		});
		
		// Close
		closeItem.addActionListener(arg0 -> {
			if (cluedoGame.getGui().exitGame()){
				System.exit(0);
			}
		});
		
	}

	/**
	 * There are multiple ways of exiting the rules menu, in either case,
	 * the text for the button needs to return to the state of "Rules"
	 */
	public static void setExitRulesFalse(){
		exitRules = false;
		rulesItem.setText("Rules");
	}
}
