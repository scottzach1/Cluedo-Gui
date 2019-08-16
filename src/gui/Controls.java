package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Controls extends JPanel {

	private final Color accentCol = Color.WHITE;
	private final Color baseCol = Color.DARK_GRAY;	
	public static final int inset = 10;
	
	private String borderTitle;
	private Console c;
	private UserInterface ui;
	private Dimension size;
	
	public Controls() {
		borderTitle = "CONTROLS";
		
		// Set the Size of the Control panel
		size = getPreferredSize();
		size.height = GUI.CONTROLS_HEIGHT;
		size.width = GUI.SCREEN_WIDTH;
		setPreferredSize(size);		
		
		// Create the boarder
		Border b = BorderFactory.createTitledBorder( BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(b);
		setBackground(baseCol);
		
		// Create and Add the two panels
		c = new Console();
		ui = new UserInterface();
		
		// Set layout and add the components
		setLayout(new BorderLayout());
		add(c, BorderLayout.WEST);
		add(ui, BorderLayout.EAST);
		
		
	}
	
	public void howManyPlayers() {
		c.howManyPlayers();
		ui.howManyPlayers();
	}
	
	


	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
	

}
