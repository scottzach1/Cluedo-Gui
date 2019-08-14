package GUIComponenets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

public class GUI extends JFrame {
	// Nothing important
	private static final long serialVersionUID = 1L;

	protected void redraw(Graphics g) {
	}

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int CANVAS_HEIGHT = screenSize.height * 2 / 3;
	public static final int CONTROLS_HEIGHT = screenSize.height / 3;
	public static final int SCREEN_WIDTH = screenSize.width;
	private static final int TEXT_OUTPUT_ROWS = 5;

	private JFrame frame;
	private Canvas canvas;
	private Controls controls;
	private JMenuBar menuBar;

	public GUI() {
		initialise();
	}

	private void initialise() {
		// Create the frame
		frame = new JFrame("CLUDEO GAME");
		frame.setSize(screenSize.getSize());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Create the layout
		canvas = new Canvas();
		controls = new Controls();

		// Add panels to frame
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(controls, BorderLayout.SOUTH);

		// Set to visible and resizable
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public void redraw(JPanel jp) {
		frame.repaint();
	}

	// Testing
	public static void main(String[] args) {
		GUI g = new GUI();
	}

}
