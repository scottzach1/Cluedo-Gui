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
	

	private final Color baseCol = new Color(100, 0, 30);

	private String borderTitle = "";
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;

	public Console() {
		components = new ArrayList<>();

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

	protected void howManyPlayers() {
		JTextArea instr = new JTextArea();
		
		// Set up the settings
		Font font = new Font("Arial", Font.BOLD, 30);		
		instr.setFont(font);
		instr.setBackground(null);	
		instr.setForeground(Color.WHITE);
		instr.setEditable(false);
		
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
