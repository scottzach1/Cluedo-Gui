package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import game.Board;
import game.CluedoGame;

public class GUI extends JFrame implements ComponentListener {
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

	// Fields: All the contents of this container
	private Canvas canvas;
	private Controls controls;
	private JMenuBar menuBar;

	// Set up stuff
	private final CluedoGame cludeoGame;
	private final Board board;

	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------

	public GUI(CluedoGame aCluedoGame) {
		super("CLUEDO GAME");
		cludeoGame = aCluedoGame;
		board = cludeoGame.getBoard();

		// Create the frame
		setPreferredSize(screenSize.getSize());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		addComponentListener(this);

		// Set to visible and resizable
		setResizable(true);
		setVisible(true);
		pack();
	}

	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------

	public void addLayoutComponents(){
		// Create the layout
		canvas = new Canvas (cludeoGame);
		controls = new Controls(cludeoGame);
		menuBar = new MenuOptions();

		// Add panels to frame
		setJMenuBar(menuBar);
		add(canvas, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
	}


	// Display the main menu
	public void mainMenu() {
		clear();
		canvas.mainMenu();
		controls.mainMenu();
		redraw();
	}

	// Displays the main menu for each panel
	public void howManyPlayers() {
		clear();
		canvas.howManyPlayers();
		controls.howManyPlayers();
		redraw();
	}

	public void createUser(int tempUserNum) {
		clear();
		canvas.createUser(tempUserNum);
		controls.createUser(tempUserNum);
		redraw();
	}

	public void selectCharacter(String tempUserName) {
		clear();
		canvas.selectCharacter(tempUserName);
		controls.selectCharacter(tempUserName);
		redraw();
	}

	public void gameSetup(){
		clear();
		canvas.renderBoard();
		controls.addContainers();
		redraw();
	}

	public void runGame(){
		clear();
		controls.runGame();
		redraw();
	}

	// --------------------------------------------------
	// HELPFUL METHODS
	// --------------------------------------------------
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

	public void redraw() {
		revalidate();
		repaint();
	}

	public void clear() {
		canvas.clearComponents();
		controls.clear();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		redraw();
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}
}
