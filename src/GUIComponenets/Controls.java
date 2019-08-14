package GUIComponenets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Controls extends JPanel {
	
	private String borderTitle = "CONTROLS";
	
	public Controls() {		
		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.height = GUI.CONTROLS_HEIGHT;
		setPreferredSize(size);		
		
		// Create the boarder
		Color accentCol = Color.WHITE;
		Color baseCol = Color.DARK_GRAY;
		Border b = BorderFactory.createTitledBorder( BorderFactory.createEmptyBorder(), borderTitle, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(b);
		setBackground(baseCol);
		
		// Create and Add the two panels
		Console c = new Console();
		UserInterface ui = new UserInterface();
		
		// Set layout
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(0, 5, 5, 5);
		
		gc.weightx = 0.5;
		gc.weighty = 0.5;		
		gc.gridx = 0;	
		add(c, gc);
		
		gc.weightx = 1;
		gc.weighty = 1;		
		gc.gridx = 1;	
		add(ui, gc);
		
		
		
		
	}

}
