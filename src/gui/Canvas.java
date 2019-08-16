package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Canvas extends JPanel {
	

	private final Color accentCol = Color.BLACK;
	private final Color baseCol = Color.WHITE;

	private String borderTitle;
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;

	public Canvas() {
		borderTitle = "MAIN MENU";
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// -------------------------------
	// Control components
	// -------------------------------




	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
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