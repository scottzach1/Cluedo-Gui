package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import game.Sprite;
import game.User;

public class GUI extends JFrame {
	// Nothing important
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------

	// Dimension of the frame, based on screen size
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int CANVAS_HEIGHT = screenSize.height * 2 / 3;
	public static final int CONTROLS_HEIGHT = screenSize.height / 3;
	public static final int SCREEN_HEIGHT = screenSize.height;
	public static final int SCREEN_WIDTH = screenSize.width;

	// Allows for states
	private int state;

	// Fields: All the contents of this container
	private JFrame frame;
	private Canvas canvas;
	private Controls controls;
	private JMenuBar menuBar;
	private int playerAmount;
	private List<User> users;
	private String tempUserName;
	private Sprite.SpriteAlias tempCharacterChoice;
	private int tempUserNum;

	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------

	public GUI() {
		state = 0;
		tempUserNum = 0;
		tempUserName = "";

		// Create the frame
		frame = new JFrame("CLUEDO GAME");
		frame.setSize(screenSize.getSize());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Create the layout
		canvas = new Canvas(this);
		controls = new Controls(this);
		menuBar = new MenuOptions();

		// Add panels to frame
		frame.setJMenuBar(menuBar);
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(controls, BorderLayout.SOUTH);

		// Set to visible and resizable
		frame.setResizable(true);
		frame.setVisible(true);
		frame.pack();
	}

	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------

	public void runGUI() {
		if (state == 0)
			mainMenu();
		else if (state == 1)
			howManyPlayers();
		else if (state == 2)
			createUser();
	}

	// Display the main menu
	private void mainMenu() {
		clear();
		canvas.mainMenu();
		controls.mainMenu();
		redraw();
	}

	// Displays the main menu for each panel
	private void howManyPlayers() {
		clear();
		canvas.howManyPlayers();
		controls.howManyPlayers();
		redraw();
	}

	private void createUser() {
		clear();
		canvas.createUser(tempUserNum);
		controls.createUser(tempUserNum);
		redraw();
	}

	public void selectCharacter() {
		clear();
		canvas.selectCharacter(tempUserName);
		controls.selectCharacter(tempUserName);
		redraw();
	}

	// --------------------------------------------------
	// HELPFUL METHODS
	// --------------------------------------------------
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Controls getControls() {
		return controls;
	}

	public void setControls(Controls controls) {
		this.controls = controls;
	}

	public void setPlayerAmount(int playerAmount) {
		this.playerAmount = playerAmount;
	}

	public void setTempUserName(String un) {
		this.tempUserName = un;
	}

	public void setTempCharacter(Sprite.SpriteAlias sa) {
		tempCharacterChoice = sa;
	}

	public void nextTempUserNum() {
		tempUserNum++;
	}

	public void redraw() {
		frame.revalidate();
		frame.repaint();
	}

	public void clear() {
		canvas.clear();
		controls.clear();
	}

	public void nextState() {
		state++;
		runGUI();
	}

	// Testing
	public static void main(String[] args) {
		GUI g = new GUI();
		g.runGUI();
	}

}
