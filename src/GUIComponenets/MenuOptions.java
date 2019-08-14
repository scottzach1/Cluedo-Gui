package GUIComponenets;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class MenuOptions extends JMenuBar {

	public MenuOptions() {		
		// Set the Size of the Control panel
		Dimension size = getPreferredSize();
		size.width = GUI.SCREEN_WIDTH;
		size.height = 30;
		setPreferredSize(size);
		setFont(new Font("Serif", Font.BOLD, 18));
		
		// Create the file menu
		JMenu file = new JMenu("File");
		JMenuItem restartItem = new JMenuItem("Restart");
		JMenuItem closeItem = new JMenuItem("Close");
		
		JMenu help = new JMenu("Help");
		JMenuItem rulesItem = new JMenuItem("Rules");
		JMenuItem optionsItem = new JMenuItem("Options");
		
		// Add buttons to directories
		file.add(restartItem);
		file.add(closeItem);
		
		help.add(rulesItem);
		help.add(optionsItem);
		
		add(file);
		add(help);
		
		
		
		// Button actions:
		
		// Close
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}			
		});
		
	}
}
