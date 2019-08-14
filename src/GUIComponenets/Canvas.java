package GUIComponenets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Canvas extends JPanel {

	private String borderTitle = "GAME MAP";

	public Canvas(){
		// Set the Size of the canvas panel
		Dimension size = getPreferredSize();
		size.height = GUI.CANVAS_HEIGHT;
		size.width = GUI.SCREEN_WIDTH;
		setPreferredSize(size);
		
		// Create the boarder
		Color accentCol = Color.BLACK;
		Color baseCol = Color.WHITE;
		Border b = BorderFactory.createTitledBorder( BorderFactory.createMatteBorder(2,0,0,0, accentCol), borderTitle, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(b);
		setBackground(baseCol);
	}
	
	public void MainMenu() {
		
	}
	
	public void makeCells() {
		// TODO: Not quite sure how to draw these yet
	}
}
