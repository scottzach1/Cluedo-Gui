package GUIComponenets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GUI extends JFrame {
	// Nothing important
	private static final long serialVersionUID = 1L;

	protected void redraw(Graphics g) {
	}

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int CANVAS_HEIGHT = screenSize.height * 2 / 3;
	public static final int CONTROLS_HEIGHT = screenSize.height / 3;
	public static final int SCREEN_HEIGHT = screenSize.height;
	public static final int SCREEN_WIDTH = screenSize.width;
	private static final int TEXT_OUTPUT_ROWS = 5;

	private JFrame frame;
	private Canvas canvas;
	private Controls controls;
	private JMenuBar menuBar;

	public GUI() {
		initialise();
		howManyPlayers();
	}

	// Creates the base of all the components
	private void initialise() {
		// Create the frame
		frame = new JFrame("CLUDEO GAME");
		frame.setSize(screenSize.getSize());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Create the layout
		canvas = new Canvas();
		controls = new Controls();
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
	
	// Displays the main menu for each panel
	private void howManyPlayers() {
		canvas.howManyPlayers();
		controls.howManyPlayers();		
		redraw();
	}
	
	
	
	
	// ----------------------------
	// HELPFUL: Getters, Setters, Other
	// ----------------------------


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
	
	public final JFrame getGui() {
		return this;
	}

	public void redraw() {
		frame.revalidate();
		frame.repaint();
	}
	


	// Testing
	public static void main(String[] args) {
		GUI g = new GUI();
	}

}
