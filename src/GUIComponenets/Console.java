package GUIComponenets;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class Console extends JPanel {

	
	private String borderTitle = "";
	
	public Console() {
		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.width = GUI.SCREEN_WIDTH / 3;
		size.height = GUI.CONTROLS_HEIGHT;
		setPreferredSize(size);		
		
		// Create the boarder
		Color shadow = new Color(180, 205, 105);
		Color highlight = new Color(255, 255, 185);
		Color baseCol = new Color(230, 255, 135);
		Border b1 = BorderFactory.createBevelBorder(BevelBorder.RAISED, highlight, shadow);
		Border b2 = BorderFactory.createBevelBorder(BevelBorder.RAISED, highlight, shadow);
		Border b3 = BorderFactory.createCompoundBorder(b1, b2);
		setBorder(b3);
		setBackground(baseCol);
	}
	
	

}
