package GUIComponenets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class UserInterface extends JPanel {
	
	private final Color baseCol = new Color(76, 74, 75);
	
	private String borderTitle = "UI";
	private GridBagConstraints gc;
	private Dimension size;
	
	public UserInterface() {
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
	
	
	protected void howManyPlayers() {
		
		// Set up the button group and placement
		ButtonGroup group = new ButtonGroup();
		gc.weightx = 2;
		gc.weighty = 2;		
		gc.gridy = 0;		
		
		for (int i = 0; i < 6; i++) {
//			 ImageIcon normal = new ImageIcon("Assets/normal_check_box.png");
//			 ImageIcon selected = new ImageIcon("Assets/selected_check_box.png");
			 
			 JCheckBox b = new JCheckBox();
//			 b.setIcon(normal);
//			 b.setSelectedIcon(selected);
			 
			 group.add(b);
			 
			 gc.gridx = i;
			 add(b, gc);
		}		
		
		// Create the submit button
		gc.weighty = 0.5;		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 6;
		
		JButton submit = new JButton("SUBMIT");
		
		add(submit, gc);
		
	}
}
