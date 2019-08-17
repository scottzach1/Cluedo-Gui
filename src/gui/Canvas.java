package gui;

import game.Board;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Canvas extends JPanel {
	
	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------

	private final Color accentCol = Color.BLACK;
	private final Color baseCol = Color.WHITE;

	private String borderTitle;
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;
	
	
	
	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------

	public Canvas() {
		borderTitle = " - - ";
		components = new ArrayList<>();

		// Set the Size of the canvas panel
		size = getPreferredSize();
		size.height = GUI.CANVAS_HEIGHT;
		size.width = GUI.SCREEN_WIDTH;
		setPreferredSize(size);

		// Create the boarder
		Border b = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(b);
		setBackground(baseCol);

		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
	}
	
	
	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------
	
	public void mainMenu() {
		Image image = (new ImageIcon("title.png")).getImage();
		image = image.getScaledInstance(size.width, size.height,  java.awt.Image.SCALE_SMOOTH);
		JLabel titleImg = new JLabel(new ImageIcon(image));
		
		components.add(titleImg);
		revalidateComponents(1);
	}
	
	
	public void howManyPlayers() {
		// Create a text instructions for the user to follow
		JTextArea instructions = new JTextArea();
		
		// Create the font for the text
		Font font = new Font("Arial", Font.BOLD, 40);
		instructions.setFont(font);
		
		// Add the text to the instructions
		instructions.append("How many players today?");
		
		// Set the preferred size such that 40pt text can fit
		// (Only thing on screen means that it can take up everything
		instructions.setBackground(null);
		instructions.setEditable(false);
		
		components.add(instructions);
		revalidateComponents(1);		
	}
	
	
	public void createUser(int playerNum) {
		// Create a text instructions for the user to follow
		JTextArea instructions = new JTextArea();
		
		// Create the font for the text
		Font font = new Font("Arial", Font.BOLD, 40);
		instructions.setFont(font);
		
		// Add the text to the instructions
		instructions.append("Player " + playerNum + ", what is your preferred name?");
		
		// Set the preferred size such that 40pt text can fit
		// (Only thing on screen means that it can take up everything
		instructions.setBackground(null);
		instructions.setEditable(false);
		
		components.add(instructions);
		revalidateComponents(1);		
	}	
	
	
	
	public void selectCharacter(String userName) {
		// Create a text instructions for the user to follow
		JTextArea instructions = new JTextArea();
		
		// Create the font for the text
		Font font = new Font("Arial", Font.BOLD, 40);
		instructions.setFont(font);
		
		// Add the text to the instructions
		instructions.append(userName + ", who are you playing as?");
		
		// Set the preferred size such that 40pt text can fit
		// (Only thing on screen means that it can take up everything
		instructions.setBackground(null);
		instructions.setEditable(false);
		
		components.add(instructions);
		revalidateComponents(1);		
	}
	

	
	// --------------------------------------------------
	// HELPFUL METHODS
	// --------------------------------------------------


	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
	
	public void clear() {
		components.clear();
		removeAll();
	}

	public void drawBoard(Board board) {
		int cellWidth = Math.min(getWidth() / board.getCols(), getHeight() / board.getRows());
		Icon icon = new ImageIcon("");

	}

	private void revalidateComponents(int cols) {

		if (components.size() > 0) {
			gc.gridy = 0;
			for (int i = 0; i < components.size(); i++) {
				gc.gridx = i % cols;
				gc.gridy = i / cols;
				if (components.get(i) != null)
					add(components.get(i), gc);
			}
		} else {
			this.removeAll();
		}
	}
}
