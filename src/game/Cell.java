package game;

import extra.CombinedImageIcon;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class Cell extends JLabel implements MouseListener {

	/**
	 * An Enum defining the different possible directions of neighbours of a Game.Cell.
	 */
	public enum Direction {
		NORTH, EAST, SOUTH, WEST;
		Direction reverse() { return values()[ordinal() + ((ordinal() > 1) ? -2 : 2)]; }
	}

	/**
	 * An Enum defining the different kinds of Cells in the Game.
	 */
	public enum Type {ROOM, HALL, START_PAD, VOID, CELLAR, WALL, UNKNOWN}

	Cell go(Cell[][] cells, Direction dir) {
		switch (dir) {
			case NORTH:
				return (row > 0) ? cells[row - 1][col] : null;
			case SOUTH:
				return (row + 1 < cells.length) ? cells[row + 1][col] : null;
			case WEST:
				return (col > 0) ? cells[row][col - 1] : null;
			case EAST:
				return (col + 1 < cells[0].length) ? cells[row][col + 1] : null;
			default:
				return null;
		}
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Game.Cell Attributes
	private Board board;
	private Map<String, ImageIcon> icons;
	private ImageIcon prevIcon;
	private Sprite sprite;
	private Room room;
	private int col;
	private int row;
	private Type type;
	private HashMap<Direction, Cell> neighbors;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Game.Cell: The Constructor for a new Game.Cell.
	 *
	 * @param row  Row of the Game.Cell.
	 * @param col  Column of the Game.Cell.
	 * @param type The type of Game.Cell. Ie, Type.Wall
	 */
	public Cell(int row, int col, Cell.Type type, Board board) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.board = board;
		neighbors = new HashMap<>();
		icons = board.getScaledImageIcons();
		this.addMouseListener(this);
	}

	public Cell render() {
		ImageIcon base;
		if (board.highlightedCells.contains(this))
			base = icons.get(parseHighLightedImageIcon(type));
		else base = icons.get(parseImageIcon(type));

		List<ImageIcon> layers = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			if (neighbors.get(dir) == null) layers.add(icons.get(parseWallIcon(dir)));
		}

		if (board.visitedCells.contains(this)) layers.add(icons.get("cell_visited.png"));

		if (sprite != null) {
			if (sprite.matchesType(board.cluedoGame.getCurrentUser().getSprite().getSpriteAlias()))
				base = icons.get(sprite.getCell());
			else layers.add(icons.get(sprite.getMarker()));
		}
		setIcon(prevIcon = new CombinedImageIcon(base, layers));
		return this;
	}

	static String parseWallIcon(Direction dir) {
		switch (dir) {
			case WEST:
				return "wall_left.png";
			case EAST:
				return "wall_right.png";
			case NORTH:
				return "wall_top.png";
			case SOUTH:
				return "wall_bot.png";
			default:
				return "cell_void.png";
		}
	}

	static String parseImageIcon(Cell.Type type) {
		switch (type) {
			case CELLAR:
				return "cell_green.png";
			case HALL:
				return "cell_grey.png";
			case START_PAD:
				return "cell_grey.png";
			case ROOM:
				return "cell_red.png";
			case VOID:
				return "cell_void.png";
			default:
				return "cell_unknown.png";
		}
	}

	static String parseHighLightedImageIcon(Cell.Type type) {
		switch (type) {
			case HALL:
				return "cell_grey_highlighted.png";
			case START_PAD:
				return "cell_grey_highlighted.png";
			case ROOM:
				return "cell_red_highlighted.png";
			default:
				return parseImageIcon(type);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (board.pathFinder.getPath().stream().anyMatch(board.visitedCells::contains)) {
			System.out.println("MATCH!");
			board.cluedoGame.getGui().infeasibleMove();
			board.cluedoGame.getGui().redrawDice();
			return;
		} else if (board.highlightedCells.isEmpty()) {
			System.out.println("INFEASIBLE!");
			board.cluedoGame.getGui().infeasibleMove();
			board.cluedoGame.getGui().redrawDice();
			return;
		}

		board.cluedoGame.removeMovesLeft(board.pathFinder.getPath().size());

		board.pathFinder.getPath().forEach(cell -> {
			System.out.println(cell.getStringCoordinates());
//				try {
//					Thread.sleep(300);
//				} catch (InterruptedException ex) {
//					ex.printStackTrace();
//				}
//				board.moveUser(board.cluedoGame.getCurrentUser(), cell);
//				board.cluedoGame.getGui().redraw();
//				board.getStream().forEach(Cell::render);
		});

		board.visitedCells.addAll(board.highlightedCells);

		board.highlightedCells.clear();
		board.highlightedRooms.clear();

		board.moveUser(board.cluedoGame.getCurrentUser(), this);
		board.getStream().forEach(Cell::render);

		board.cluedoGame.getGui().redraw();
		board.cluedoGame.getGui().redrawDice();

		board.pathFinder.getPath().clear();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (isType(Type.VOID)) return;
		CluedoGame cluedoGame = board.cluedoGame;
		int movesLeft = cluedoGame.getMovesLeft();
		Cell startingCell = cluedoGame.getCurrentUser().getSprite().getPosition();

		board.highlightedCells.clear();
		board.highlightedRooms.clear();
		board.pathFinder.getPath().clear();

		PathFinder pathFinder = board.pathFinder;

		boolean success;
		if (CluedoGame.shortestPath)
				success = movesLeft >= pathFinder.findShortestPath(startingCell, this);
		else 	success = pathFinder.findExactPath(startingCell, this, movesLeft);

		if (!success) {
			board.highlightedRooms.forEach(room -> board.highlightedCells.addAll(room.getCells()));
			System.out.println(board.highlightedRooms.size());
			board.highlightedCells.clear();
			board.highlightedRooms.clear();
		}  else {
			board.highlightedRooms.forEach(room -> board.highlightedCells.addAll(room.getCells()));
		}
		board.getStream().forEach(Cell::render);
		if (!success) setIcon(new CombinedImageIcon(prevIcon, icons.get("cell_invalid.png")));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		board.highlightedRooms.clear();
		board.highlightedCells.clear();
		board.getStream().forEach(Cell::render);
		render();
		repaint();
	}

	// ------------------------
	// INTERFACE
	// ------------------------


	public boolean hasRoom() { return room != null; }
	public boolean missingRoom() { return type == Type.ROOM && room == null; }
	public boolean isType(Type type) { return type == getType(); }
	/**
	 * getType: Get the Type of the Game.Cell.
	 * @return the Type of the Game.Cell.
	 */
	public Type getType() {return type;}

	/**
	 * Get the Game.Sprite on the Game.Cell, if any.
	 * @return Get any Game.Sprite on the Game.Cell currently.
	 */
	public Sprite getSprite() {return sprite;}

	/**
	 * setSprite: Place a sprite on the Game.Cell.
	 * @param sprite Game.Sprite to place on Game.Cell.
	 */
	void setSprite(Sprite sprite) {this.sprite = sprite; }

	/**
	 * getRoom: Get the Game.Room a cell is in, if any.
	 * @return The Game.Room of the Game.Cell.
	 */
	public Room getRoom() {return room;}

	/**
	 * setRoom: Sets the Game.Room of this Game.Cell.
	 * @param room The Game.Room to set for this Game.Cell.
	 */
	void setRoom(Room room) {
		this.room = room;
		if (room == null || room.getCells().contains(this)) return;
		room.addCell(this);
	}

	/**
	 * getCol: Get the Column of the Game.Cell.
	 * @return Col of Game.Cell.
	 */
	int getCol() {return col;}

	/**
	 * getRow: Get the Row of the Game.Cell.
	 * @return Row of Game.Cell.
	 */
	int getRow() {return row;}

	/**
	 * getNeighbours: Return a Map of all the neighbours of the Game.Cell, with Direction as the Key.
	 * @return Map of Neighbours.
	 */
	public HashMap<Direction, Cell> getNeighbors(){return neighbors;}

	/**
	 * setNeighbour: Set a neighbour for a direction.
	 * @param dir Direction to add neighbour.
	 * @param cell Game.Cell to add as neighbour.
	 */
	void setNeighbor(Direction dir, Cell cell) {neighbors.put(dir, cell);}

	/**
	 * getType: Get the type of Game.Cell based on the corresponding character on the raw data map. (MapBase.txt)
	 * @param c char of Game.Cell.
	 * @return Type of Game.Cell.
	 */
	static Type getType(char c) {
		if (c == 'M') return Type.CELLAR;
		if (c == ' ') return Type.VOID;
		if (c == 'X') return Type.ROOM;
		if (c == 'Y') return Type.HALL;
		if (c == '_') return Type.HALL;
		if (Pattern.matches("\\d", c + "")) return Type.START_PAD;
		if (Pattern.matches("[A-Z]", c + "")) return Type.ROOM;
		return Type.UNKNOWN;
	}

	/**
	 * getStringCoordinates: Return a String of the coordinates of a Game.Cell. Ie, H15
	 * @return String of Game.Cell coordinates.
	 */
	public String getStringCoordinates() { return "" + ((char) (col + 'A')) + (row + 1); }

	public String toString() {
		if (sprite != null) return sprite.toString();
		if (type == Type.ROOM) return "_";
		if (type == Type.HALL) return "_";
		if (isType(Type.VOID)) return "/";
		else if (type == Type.START_PAD) return "$";
		return "ERROR ON TYPE";
	}

	/**
	 * sameCell: Checks whether both cells are the same, or share the same non null room.
	 * @param other second cell to compare.
	 * @return boolean true if same, false otherwise.
	 */
	public boolean sameCell(Cell other) {
		return sameRoom(other) || this == other;
	}

	/**
	 * sameRoom: Checks whether both Cells share a non null room.
	 * @param other second cell to compare.
	 * @return true if non null room shared, false other wise.
	 */
	public boolean sameRoom(Cell other) {
		return (hasRoom() && this.getRoom() == other.getRoom());
	}
}
