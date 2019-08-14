package GUIComponenets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class UserInterface extends JPanel {
	
	private String borderTitle = "UI";
	
	public UserInterface() {	
		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.width = GUI.SCREEN_WIDTH * 2 / 3;
		size.height = GUI.CONTROLS_HEIGHT;
		setPreferredSize(size);		
		
		// Create the boarder
		Color shadow = new Color(185, 160, 115);
		Color highlight = new Color(255, 255, 215);
		Color baseCol = new Color(235, 210, 165);
		Border b1 = BorderFactory.createBevelBorder(BevelBorder.RAISED, highlight, shadow);
		Border b2 = BorderFactory.createBevelBorder(BevelBorder.RAISED, highlight, shadow);
		Border b3 = BorderFactory.createCompoundBorder(b1, b2);
		setBorder(b3);
		setBackground(baseCol);
	}

}
