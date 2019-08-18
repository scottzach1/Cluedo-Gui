package gui;

import game.CluedoGame;
import game.Sprite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Controls extends JPanel {

	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------

	private final Color accentCol = Color.WHITE;
	private final Color baseCol = Color.DARK_GRAY;
	public static final int inset = 10;

	private String borderTitle;
	private Console console;
	private UserInterface userInterface;
	private GridBagConstraints gc;
	private final CluedoGame cluedoGame;

	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------

	public Controls(CluedoGame parent) {
		borderTitle = "CONTROLS";
		cluedoGame = parent;

		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.height = GUI.CONTROLS_HEIGHT;
		size.width = GUI.SCREEN_WIDTH;
		setPreferredSize(size);

		// Create the boarder
		Border border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(border);
		setBackground(baseCol);

		// Set the layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
	}

	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------

	/**
	 * mainMenu:
	 */
	public void mainMenu() {
		gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;

		JButton play = new JButton("PLAY");
		play.setPreferredSize(new Dimension(getWidth() / 10, getHeight() / 5));
		play.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 20)));
		
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.nextState();
			}
			
		});

		add(play, gc);
	}

	/**
	 * howManyPlayers:
	 */
	protected void howManyPlayers() {
		gc = new GridBagConstraints();

		// Set up the button group and placement
		ButtonGroup group = new ButtonGroup();
		gc.weightx = 2;
		gc.weighty = 2;
		gc.gridy = 0;

		int checkBoxSize = Math.min(Math.min(getWidth() / 4, getHeight() / 3), 50);

		for (int i = 0; i < 4; i++) {
			// NORMAL IMAGE
			Image image = (new ImageIcon("normal_check_box.png")).getImage();
			image = image.getScaledInstance(checkBoxSize, checkBoxSize, java.awt.Image.SCALE_SMOOTH);
			ImageIcon normal = new ImageIcon(image);
			// SELECTED IMAGE
			Image image2 = (new ImageIcon("selected_check_box.png")).getImage();
			image2 = image2.getScaledInstance(checkBoxSize, checkBoxSize, java.awt.Image.SCALE_SMOOTH);
			ImageIcon selected = new ImageIcon(image2);

			JCheckBox b = new JCheckBox((i + 3) + "");
			b.setIcon(normal);
			b.setSelectedIcon(selected);
			b.setFont(new Font("Arial", Font.PLAIN, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 40)));
			b.setBackground(null);
			b.setForeground(Color.WHITE);
			b.setActionCommand((i+3)+"");

			group.add(b);

			gc.gridx = i;
			gc.anchor = GridBagConstraints.CENTER;
			add(b, gc);
		}

		// Create the submit button
		gc.fill = GridBagConstraints.BOTH;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 4;

		JButton submit = new JButton("SUBMIT");

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int players = Integer.parseInt(group.getSelection().getActionCommand());
					cluedoGame.setPlayerAmount(players);
					cluedoGame.nextState();
				} catch (Exception e) {}
			}

		});

		add(submit, gc);

	}
	
	public void createUser(int playerNum) {
		gc = new GridBagConstraints();
		
		// Create label and text field
		JLabel name = new JLabel("NAME: ");	
		JTextField nameField = new JTextField(20);
		
		// Setup the components
		name.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 30)));
		name.setForeground(accentCol);
		nameField.setBackground(null);
		
		nameField.setFont(new Font("Arial", Font.BOLD, Math.min(Math.min(getWidth() / 4, getHeight() / 3), 30)));
		nameField.setForeground(accentCol);
		nameField.setCaretColor(accentCol);
		nameField.setBackground(baseCol);
		
		// Add the nameField 
		nameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cluedoGame.setTempUserName(nameField.getText());
				cluedoGame.getGui().selectCharacter(nameField.getText());
			}
		});
		
		// Add the name
		gc.anchor = GridBagConstraints.LINE_END;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(0,0,0,5);
		
		gc.gridx = 0;
		gc.gridy = 0;
		add(name, gc);

		// add the field
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0,5,0,0);
		gc.gridx = 1;
		add(nameField, gc);
		
	}
	
	public void selectCharacter(String userName) {
		gc = new GridBagConstraints();

		// Create drop down menu
		JComboBox spriteOptions = new JComboBox(new Vector< Sprite.SpriteAlias>(cluedoGame.getAvailableSprites()));
		spriteOptions.setPreferredSize(new Dimension(getWidth() / 10, getHeight() /10));

		// Create submit button
		JButton submit = new JButton("SUBMIT");
		submit.setPreferredSize(new Dimension(getWidth() / 10, getHeight() /10));

		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cluedoGame.setTempSprite((Sprite.SpriteAlias) spriteOptions.getSelectedItem());
				cluedoGame.removeAvailableSprite((Sprite.SpriteAlias) spriteOptions.getSelectedItem());
				cluedoGame.nextTempUserNum();
			}
		});

		gc.weightx = 1;
		gc.weighty = 1;
		// add the components to their locations
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0,0,0,10);
		gc.anchor = GridBagConstraints.LINE_END;
		add(spriteOptions, gc);
		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0,10,0,0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(submit, gc);

	}

	/**
	 * addContainers:
	 */
	public void addContainers() {

		setLayout(new BorderLayout());

		// Create and Add the two panels
		console = new Console(cluedoGame);
		userInterface = new UserInterface(cluedoGame);

		add(console, BorderLayout.WEST);
		add(userInterface, BorderLayout.EAST);
	}

	/**
	 *
	 */
	public void runGame(){
		int dieOne = cluedoGame.rollDie();
		int dieTwo = cluedoGame.rollDie();
		cluedoGame.setMovesThisTurn(dieOne + dieTwo);
		console.drawDice(dieOne, dieTwo);
	}

	// --------------------------------------------------
	// HELPFUL METHODS
	// --------------------------------------------------

	/**
	 * setBorderTitle:
	 */
	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}

	/**
	 * clear:
	 */
	public void clear() {
		removeAll();
	}

}
