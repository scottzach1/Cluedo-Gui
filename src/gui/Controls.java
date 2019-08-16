package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Controls extends JPanel {

	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------

	private final Color accentCol = Color.WHITE;
	private final Color baseCol = Color.DARK_GRAY;
	public static final int inset = 10;

	private String borderTitle;
	private Console c;
	private UserInterface ui;
	private Dimension size;

	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------

	public Controls() {
		borderTitle = "CONTROLS";

		// Set the Size of the Control panel
		size = getPreferredSize();
		size.height = GUI.CONTROLS_HEIGHT;
		size.width = GUI.SCREEN_WIDTH;
		setPreferredSize(size);

		// Create the boarder
		Border b = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(b);
		setBackground(baseCol);

		// Set the layout
		setLayout(new BorderLayout());
	}

	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------

	/**
	 * mainMenu:
	 */
	public void mainMenu() {
		// Set layout
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;

		JButton play = new JButton("PLAY");
		play.setPreferredSize(new Dimension(size.width / 10, size.height / 5));
		play.setFont(new Font("Arial", Font.BOLD, 20));

		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				GUI g = (GUI) GUI.getFrames()[0];
				g.next();
			}

		});

		add(play, gc);
	}

	/**
	 * addContainers:
	 */
	public void addContainers() {

		// Create and Add the two panels
		c = new Console();
		ui = new UserInterface();

		add(c, BorderLayout.WEST);
		add(ui, BorderLayout.EAST);
	}

	/**
	 * howManyPlayers:
	 */
	protected void howManyPlayers() {

		// Set the layout
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;

		// Set up the button group and placement
		ButtonGroup group = new ButtonGroup();
		gc.weightx = 2;
		gc.weighty = 2;
		gc.gridy = 0;

		for (int i = 0; i < 6; i++) {
			// NORMAL IMAGE
			Image image = (new ImageIcon("assets/normal_check_box.png")).getImage();
			image = image.getScaledInstance(size.width / 12, size.height / 2, java.awt.Image.SCALE_SMOOTH);
			ImageIcon normal = new ImageIcon(image);
			// SELECTED IMAGE
			Image image2 = (new ImageIcon("assets/selected_check_box.png")).getImage();
			image2 = image2.getScaledInstance(size.width / 12, size.height / 2, java.awt.Image.SCALE_SMOOTH);
			ImageIcon selected = new ImageIcon(image2);

			JCheckBox b = new JCheckBox((i + 1) + "");
			b.setIcon(normal);
			b.setSelectedIcon(selected);
			b.setFont(new Font("Arial", Font.PLAIN, 40));
			b.setBackground(null);
			b.setForeground(Color.WHITE);
			b.setActionCommand((i+1)+"");

			group.add(b);

			gc.gridx = i;
			add(b, gc);
		}

		// Create the submit button
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 6;

		JButton submit = new JButton("SUBMIT");

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int players = Integer.parseInt(group.getSelection().getActionCommand());
					GUI g = (GUI) GUI.getFrames()[0];
					g.setPlayerAmount(players);
					g.next();
				} catch (Exception e) {}
			}

		});

		add(submit, gc);

	}

	// --------------------------------------------------
	// HELPFUL METHODS
	// --------------------------------------------------

	/**
	 * setBorderTitle:
	 */
	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}

	/**
	 * clear:
	 */
	public void clear() {
		removeAll();
	}

}
