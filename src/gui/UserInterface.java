package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class UserInterface extends JPanel {
	
	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------
	
	private final Color baseCol = new Color(76, 74, 75);
	
	private String borderTitle = "UI";
	private GridBagConstraints gc;
	private Dimension size;
	private final GUI gui;
	
	
	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------
	
	public UserInterface(GUI parent) {
		gui = parent;
		// Set the Size of the Control panel
		size = getPreferredSize();
		size.width = (GUI.SCREEN_WIDTH * 2 / 3) - (Controls.inset / 2);
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
		gc.fill = GridBagConstraints.BOTH;
	}
	
	
	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------
	
	public void clear() {
		removeAll();
	}
}
