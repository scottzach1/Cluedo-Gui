package GUIComponenets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

public class UserInterface extends JPanel {
	
	private final Color baseCol = new Color(0, 100, 15);
	
	private String borderTitle = "UI";
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;
	
	public UserInterface() {	
		components = new ArrayList<>();
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
		gc.weightx = 1;
		gc.weighty = 1;
	}
	
	
	protected void howManyPlayers() {
		
		ButtonGroup group = new ButtonGroup();
		
		// Create the buttons
		JRadioButton b1 = new JRadioButton("1");
		JRadioButton b2 = new JRadioButton("2");
		JRadioButton b3 = new JRadioButton("3");
		JRadioButton b4 = new JRadioButton("4");
		JRadioButton b5 = new JRadioButton("5");
		JRadioButton b6 = new JRadioButton("6");
		
		//Setup the buttons ---
		
		//Backgrounds
		b1.setBackground(null);
		b2.setBackground(null);
		b3.setBackground(null);
		b4.setBackground(null);
		b5.setBackground(null);
		b6.setBackground(null);
		//Foregrounds
		b1.setForeground(Color.WHITE);
		b2.setForeground(Color.WHITE);
		b3.setForeground(Color.WHITE);
		b4.setForeground(Color.WHITE);
		b5.setForeground(Color.WHITE);
		b6.setForeground(Color.WHITE);
		
		
		
		// Add to the group
		group.add(b1);
		group.add(b2);
		group.add(b3);
		group.add(b4);
		group.add(b5);
		group.add(b6);
		
		
		// Add to the components list
		components.add(b1);
		components.add(b2);
		components.add(b3);
		components.add(b4);
		components.add(b5);
		components.add(b6);
		
		revalidateComponents(6);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

	

	// -------------------------------
	// Control components
	// -------------------------------


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
