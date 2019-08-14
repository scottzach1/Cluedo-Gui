package GUIComponenets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public class GUI extends JFrame {
	// Nothing important
	private static final long serialVersionUID = 1L;

	protected void redraw(Graphics g) {
	}

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int DEFAULT_WINDOW_HEIGHT = (int) (screenSize.getHeight() * 0.7);
	private static final int DEFAULT_WINDOW_WIDTH = (int) (screenSize.getWidth() * 0.7);
	private static final int TEXT_OUTPUT_ROWS = 5;

	private JFrame frame;
	private JPanel controls;
	private JComponent canvas;
	private JTextArea console;

	public GUI() {
		initialise();
	}

	public Dimension getDrawingAreaDimension() {
		return canvas.getSize();
	}

	private void initialise() {
		// Create the new frame
		frame = new JFrame();
		frame.setPreferredSize(screenSize);
		
		// Add the menu
		JMenuBar menuBar = new JMenuBar();
		
		
		frame.setVisible(true);
	}

	private void redraw() {
		frame.repaint();
	}

	public void display(JPanel jp) {

	}

	// Testing
	public static void main(String[] args) {
		GUI g = new GUI();
	}

}
