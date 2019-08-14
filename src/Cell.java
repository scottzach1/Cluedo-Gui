
import java.util.HashMap;
import java.util.regex.Pattern;

public class Cell {

	/**
	 * An Enum defining the different possible directions of neighbours of a Cell.
			*/
	public enum Direction {NORTH, SOUTH, EAST, WEST}

	/**
	 * An Enum defining the different kinds of Cells in the Game.
	 */
	public enum Type {ROOM, WALL, BLANK, START_PAD}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Cell Attributes
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
	 * Cell: The Constructor for a new Cell.
	 * @param row Row of the Cell.
	 * @param col Column of the Cell.
	 * @param type The type of Cell. Ie, Type.Wall
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
	 * getType: Get the Type of the Cell.
	 * @return the Type of the Cell.
	 */
	public Type getType() {return type;}

	/**
	 * Get the Sprite on the Cell, if any.
	 * @return Get any Sprite on the Cell currently.
	 */
	public Sprite getSprite() {return sprite;}

	/**
	 * setSprite: Place a sprite on the Cell.
	 * @param sprite Sprite to place on Cell.
	 */
	void setSprite(Sprite sprite) {this.sprite = sprite;}

	/**
	 * getRoom: Get the Room a cell is in, if any.
	 * @return The Room of the Cell.
	 */
	public Room getRoom() {return room;}

	/**
	 * setRoom: Sets the Room of this Cell.
	 * @param room The Room to set for this Cell.
	 */
	void setRoom(Room room) {
		this.room = room;
		if (room == null || room.getCells().contains(this)) return;
		room.addCell(this);
	}

	/**
	 * getCol: Get the Column of the Cell.
	 * @return Col of Cell.
	 */
	int getCol() {return col;}

	/**
	 * getRow: Get the Row of the Cell.
	 * @return Row of Cell.
	 */
	int getRow() {return row;}

	/**
	 * getNeighbours: Return a Map of all the neighbours of the Cell, with Direction as the Key.
	 * @return Map of Neighbours.
	 */
	public HashMap<Direction, Cell> getNeighbors(){return neighbors;}

	/**
	 * setNeighbour: Set a neighbour for a direction.
	 * @param dir Direction to add neighbour.
	 * @param cell Cell to add as neighbour.
	 */
	void setNeighbor(Direction dir, Cell cell) {neighbors.put(dir, cell);}

	/**
	 * getType: Get the type of Cell based on the corresponding character on the raw data map. (MapBase.txt)
	 * @param c char of Cell.
	 * @return Type of Cell.
	 */
	static Type getType(char c) {
		if (c == '#') return Type.WALL;
		if (Pattern.matches("\\d", c + "")) return Type.START_PAD;
		if (Pattern.matches("[A-Z]", c + "")) return Type.ROOM;		
		return Type.BLANK;
	}

	/**
	 * getStringCoordinates: Return a String of the coordinates of a Cell. Ie, H15
	 * @return String of Cell coordinates.
	 */
	public String getStringCoordinates() { return "" + ((char) (col + 'A')) + (row + 1); }

	public String toString() {
		if (sprite != null) return sprite.toString();
		if (type == Type.ROOM) return "_";
		if (type == Type.BLANK) return "_";
		else if (type == Type.WALL) return "#";
		else if (type == Type.START_PAD) return "$";		
		return "ERROR ON TYPE";
	}
}