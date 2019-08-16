package GUIComponenets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Controls extends JPanel {

	private final Color accentCol = Color.WHITE;
	private final Color baseCol = Color.DARK_GRAY;
	
	private String borderTitle;
	private Console c;
	private UserInterface ui;
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;
	
	public Controls() {
		borderTitle = "CONTROLS";
		components = new ArrayList<>();
		
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
		
		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		
		gc.insets = new Insets(0,5,5,5);
		gc.ipady = GUI.CONTROLS_HEIGHT;
		
		gc.ipadx = c.getPreferredSize().width;
		gc.weightx = 1;
		gc.weighty = 1;		
		gc.gridx = 0;	
		add(c, gc);

		gc.weightx = 2;
		gc.weighty = 2;		
		gc.gridx = 1;	
		add(ui, gc);
		
		
	}
	
	public void mainMenu() {
		c.mainMenu();
		ui.mainMenu();
	}
	
	


	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
	

}
