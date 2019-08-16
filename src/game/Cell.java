package game;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Cell {

	/**
	 * An Enum defining the different possible directions of neighbours of a Game.Cell.
			*/
	public enum Direction {NORTH, SOUTH, EAST, WEST}

	/**
	 * An Enum defining the different kinds of Cells in the Game.
	 */
	public enum Type {ROOM, WALL, HALL, START_PAD, VOID}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Game.Cell Attributes
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
	 * @param row Row of the Game.Cell.
	 * @param col Column of the Game.Cell.
	 * @param type The type of Game.Cell. Ie, Type.Wall
	 */
	public Cell(int row, int col, Cell.Type type) {
		this.row = row;
		this.col = col;
		this.type = type;
		neighbors = new HashMap<>();
	}

	// ------------------------
	// INTERFACE
	// ------------------------

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
	void setSprite(Sprite sprite) {this.sprite = sprite;}

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
		if (c == ' ') return Type.VOID;
		if (c == 'X') return Type.ROOM;
		if (c == 'Y') return Type.HALL;
		if (Pattern.matches("\\d", c + "")) return Type.START_PAD;
		if (Pattern.matches("[A-Z]", c + "")) return Type.ROOM;		
		return Type.HALL;
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
		else if (type == Type.WALL) return "#";
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
		return ((this.getRoom() != null) && this.getRoom() == other.getRoom());
	}
}