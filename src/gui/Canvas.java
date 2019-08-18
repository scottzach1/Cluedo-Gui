package gui;

import game.Board;
import game.CluedoGame;
import game.Room;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Canvas extends JPanel {
	
	// --------------------------------------------------
	// FIELDS
	// --------------------------------------------------

	private final Color accentCol = Color.BLACK;
	private final Color baseCol = Color.WHITE;

	private String borderTitle;
	private GridBagConstraints gc;
	private ArrayList<Component> components;
	private Dimension size;
	private final GUI gui;
	private int cellSize;
	private Board board;
	
	
	// --------------------------------------------------
	// CONSTRUCTOR
	// --------------------------------------------------

	public Canvas(CluedoGame parent) {
		borderTitle = "CLUEDO GAME";
		components = new ArrayList<>();
		gui = parent.getGui();
		this.board = parent.getBoard();

		// Set the Size of the canvas panel
		size = getPreferredSize();
		size.height = GUI.CANVAS_HEIGHT;
		size.width = GUI.SCREEN_WIDTH;
		setPreferredSize(size);

		// Create the boarder
		Border b = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, accentCol), borderTitle,
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 18), accentCol);
		setBorder(b);
		setBackground(baseCol);

		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
	}


	// --------------------------------------------------
	// PUBLIC METHODS
	// --------------------------------------------------
	
	public void mainMenu() {
		Image image = (new ImageIcon("title.png")).getImage();
		image = image.getScaledInstance(size.width, size.height,  java.awt.Image.SCALE_SMOOTH);
		JLabel titleImg = new JLabel(new ImageIcon(image));
		
		components.add(titleImg);
		revalidateComponents(1);
	}
	
	
	public void howManyPlayers() {
		// Create a text instructions for the user to follow
		JTextArea instructions = new JTextArea();
		
		// Create the font for the text
		Font font = new Font("Arial", Font.BOLD, 40);
		instructions.setFont(font);
		
		// Add the text to the instructions
		instructions.append("How many players today?");
		
		// Set the preferred size such that 40pt text can fit
		// (Only thing on screen means that it can take up everything
		instructions.setBackground(null);
		instructions.setEditable(false);
		
		components.add(instructions);
		revalidateComponents(1);		
	}
	
	
	public void createUser(int playerNum) {
		// Create a text instructions for the user to follow
		JTextArea instructions = new JTextArea();
		
		// Create the font for the text
		Font font = new Font("Arial", Font.BOLD, 40);
		instructions.setFont(font);
		
		// Add the text to the instructions
		instructions.append("Player " + (playerNum + 1) + ", what is your preferred name?");
		
		// Set the preferred size such that 40pt text can fit
		// (Only thing on screen means that it can take up everything
		instructions.setBackground(null);
		instructions.setEditable(false);
		
		components.add(instructions);
		revalidateComponents(1);		
	}	
	
	
	
	public void selectCharacter(String userName) {
		// Create a text instructions for the user to follow
		JTextArea instructions = new JTextArea();
		
		// Create the font for the text
		Font font = new Font("Arial", Font.BOLD, 40);
		instructions.setFont(font);
		
		// Add the text to the instructions
		instructions.append(userName + ", who are you playing as?");
		
		// Set the preferred size such that 40pt text can fit
		// (Only thing on screen means that it can take up everything
		instructions.setBackground(null);
		instructions.setEditable(false);
		
		components.add(instructions);
		revalidateComponents(1);		
	}
	

	
	// --------------------------------------------------
	// HELPFUL METHODS
	// --------------------------------------------------


	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
	
	public void clear() {
		components.clear();
		removeAll();
	}

	public void renderBoard() {
		// Sorry this is uggly, although resized have the cells actually changed size?
		if (cellSize == (cellSize = -1 + (Math.min(getWidth() / board.getCols(), getHeight() / board.getRows())))) return;

		clear();
		board.getStream().forEach(cell -> {
			Image image = cell.getIcon().getImage();
			Image newImage = image.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
			components.add(new JLabel(new ImageIcon(newImage)));
		});
		revalidateComponents(board.getCols());
		repaint();
	}

	private void revalidateComponents(int cols) {
		if (components.size() > 0) {
			gc.gridy = 0;
			for (int i = 0; i < components.size(); i++) {
				gc.gridx = i % cols;
				gc.gridy = i / cols;
				if (components.get(i) != null)
					add(components.get(i), gc);
			}
		} else {
			this.removeAll();
		}
	}

	public static void main(String[] args) {

		CluedoGame cg = new CluedoGame();
		Board b = cg.getBoard();

		// Highlight some cells to test
		Board.HIGHLIGHTED_CELLS.add(b.getCell("O2"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("P2"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("Q2"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("Q3"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("R3"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("R4"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("R5"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("R6"));
		Board.HIGHLIGHTED_CELLS.add(b.getCell("S6"));
		Board.HIGHLIGHTED_CELLS.addAll(b.getRooms().get(Room.RoomAlias.CONSERVATORY).getCells());

		Canvas c = new Canvas(cg);

		JFrame frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(c, BorderLayout.CENTER);

		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);

		c.renderBoard();
	}
}
