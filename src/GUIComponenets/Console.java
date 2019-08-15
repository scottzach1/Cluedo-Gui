package GUIComponenets;

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
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class Console extends JPanel {
	

	private final Color shadow = new Color(180, 205, 105);
	private final Color highlight = new Color(255, 255, 185);
	private final Color baseCol = new Color(230, 255, 135);

	private String borderTitle = "";
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;

	public Console() {
		components = new ArrayList<>();

		// Set the Size of the Control panel
		size = getPreferredSize();
		size.width = GUI.SCREEN_WIDTH / 3;
		size.height = GUI.CONTROLS_HEIGHT;
		setPreferredSize(size);

		// Create the boarder
		Border b1 = BorderFactory.createBevelBorder(BevelBorder.RAISED, highlight, shadow);
		Border b2 = BorderFactory.createBevelBorder(BevelBorder.RAISED, highlight, shadow);
		Border b3 = BorderFactory.createCompoundBorder(b1, b2);
		setBorder(b3);
		setBackground(baseCol);

		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
	}

	protected void mainMenu() {
		JTextArea instr = new JTextArea();
		
		// Set up the settings
		instr.setFont(new Font("Arial", Font.BOLD, 20));
		instr.setBackground(null);		
		
		// Add the text
		instr.append("Select an option here ->");
		
		
		components.add(instr);
		revalidateComponents(1);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// -------------------------------
	// Control components
	// -------------------------------

	public void revalidateComponents(int cols) {
		if (components.size() >= 0) {
			gc.gridy = 0;
			for (int i = 0; i < components.size(); i++) {
				gc.gridx = i % cols;
				gc.gridy = i / cols;
				if (components.get(i) != null)
					add(components.get(i), gc);
			}
		} else { this.removeAll(); }
	}

}
