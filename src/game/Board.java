package game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Board {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Game.Board Attributes
	private Map<Room.RoomAlias, Room> rooms;
	private Map<Sprite.SpriteAlias, Sprite> sprites;
	private Map<Weapon.WeaponAlias, Weapon> weapons;

	private Cell[][] cells;
	private int rows, cols;
	public static Sprite.SpriteAlias ACTIVE_SPRITE = Sprite.SpriteAlias.MR_GREEN;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Game.Board: The constructor for Game.Board.
	 * - Creates a new 2d array and populates with Cells.
	 * - Generates sprites and places at corresponding starting positions.
	 * - Generates Weapons and randomly allocates them to Rooms.
	 */
	public Board() {

		// Generate room cards
		rooms = new HashMap<>();
		for (Room.RoomAlias alias : Room.RoomAlias.values()) {
			rooms.put(alias, new Room(alias));
		}

		// Generate character cards
		sprites = new HashMap<>();
		for (Sprite.SpriteAlias alias : Sprite.SpriteAlias.values()) {
			sprites.put(alias, new Sprite(alias));
		}

		List<Room> roomList = new ArrayList<>(rooms.values());
		Collections.shuffle(roomList);

		// Generate Game.Weapon cards
		weapons = new HashMap<>();
		for (Weapon.WeaponAlias alias : Weapon.WeaponAlias.values()) {
			Weapon weapon = new Weapon(alias);
			weapons.put(alias, weapon);
			weapon.setRoom(roomList.remove(roomList.size() - 1));
		}

		// This are using for linking the door ways later.
		Set<Cell> doorSteps = new HashSet<>();

		// Load Map Based off file layout
		try {
			Scanner sc = new Scanner(new File("map.txt"));
			if (!sc.next().equals("MAP"))
				throw new Exception("Invalid File Type");

			// First two indexes = Row Col of map
			rows = sc.nextInt();
			cols = sc.nextInt();

			cells = new Cell[rows][cols];

			sc.next(); // skip '\r'
			sc.nextLine(); // skip _ _ _ _ _ ...

			for (int row = 0; row != rows; ++row) {
				String line = sc.nextLine();

				int lineIndex = 3; // skip to cell 01 -> # <- |# ...

				for (int col = 0; col != cols; ++col, lineIndex += 2) {
					char c = line.charAt(lineIndex);

					Cell.Type type = Cell.getType(c);
					Cell cell = new Cell(row, col, (type!= Cell.Type.START_PAD) ? type : Cell.Type.HALL);
					cells[row][col] = cell;

					if (c == 'X') continue; // This is a door, ignore for now.
					if (c == 'Y') doorSteps.add(cell); // This is a doorstep. Use later.

					if (type == Cell.Type.START_PAD) {
						try {
							Sprite currentSprite = sprites.get(Sprite.parseAliasFromOrdinalChar(c));
							currentSprite.setPosition(cell);
							cell.setSprite(currentSprite);
						} catch (Exception e) {
							System.out.println("Not a number cell!");
							// Continue as normal as if it were blank.
						}
					} else if (type == Cell.Type.ROOM) {

						Room room = rooms.get(Room.parseAliasFromChar(c));
						room.addCell(cell);
						cell.setRoom(room);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("File Exception: " + e);
		}

		for (int row = 0; row != rows; ++row) {
			for (int col = 0; col != cols; ++col) {
				Cell cell = cells[row][col];
				Cell other;

//				if (cell.getType() == Cell.Type.VOID) continue;
				if (cell.getType() == Cell.Type.ROOM && cell.getRoom() == null) {
					// This is a door, link with corresponding room and doorsteps.

					// North
					if (row > 0) {
						other = cells[row - 1][col];
						if (other.getType() == Cell.Type.ROOM && other.getRoom() != null)
							cell.setRoom(other.getRoom());
						else if (other.getType() == Cell.Type.HALL && doorSteps.contains(other)) {
								cell.setNeighbor(Cell.Direction.NORTH, other);
								other.setNeighbor(Cell.Direction.SOUTH, cell);
						}
					}

					// South
					if (row < rows - 1) {
						other = cells[row + 1][col];
						if (other.getType() == Cell.Type.ROOM && other.getRoom() != null)
							cell.setRoom(other.getRoom());
						else if (other.getType() == Cell.Type.HALL && doorSteps.contains(other)) {
							cell.setNeighbor(Cell.Direction.SOUTH, other);
							other.setNeighbor(Cell.Direction.NORTH, cell);
						}
					}

					// East
					if (col > 0) {
						other = cells[row][col - 1];
						if (other.getType() == Cell.Type.ROOM && other.getRoom() != null)
							cell.setRoom(other.getRoom());
						else if (other.getType() == Cell.Type.HALL && doorSteps.contains(other)) {
							cell.setNeighbor(Cell.Direction.WEST, other);
							other.setNeighbor(Cell.Direction.EAST, cell);
						}
					}

					// West
					if (col < cols + 1) {
						other = cells [row][col + 1];
						if (other.getType() == Cell.Type.ROOM && other.getRoom() != null)
							cell.setRoom(other.getRoom());
						else if (other.getType() == Cell.Type.HALL && doorSteps.contains(other)) {
							cell.setNeighbor(Cell.Direction.EAST, other);
							other.setNeighbor(Cell.Direction.WEST, cell);
						}
					}
				}

				// North
				if (row > 0) {
					if (linkCells(cell, other = cells[row - 1][col]))
						cell.setNeighbor(Cell.Direction.NORTH, other);
				} else if (cell.getType() != Cell.Type.HALL) cell.setNeighbor(Cell.Direction.NORTH, new Cell(-1, col, Cell.Type.UNKNOWN));

				// South
				if (row < rows - 1) {
					if (linkCells(cell, other = cells[row + 1][col]))
						cell.setNeighbor(Cell.Direction.SOUTH, other);
				} else if (cell.getType() != Cell.Type.HALL) cell.setNeighbor(Cell.Direction.SOUTH, new Cell(row + 1, col, Cell.Type.UNKNOWN));

				// East
				if (col > 0) {
					if (linkCells(cell, other = cells[row][col - 1]))
						cell.setNeighbor(Cell.Direction.WEST, other);
				} else if (cell.getType() != Cell.Type.HALL) cell.setNeighbor(Cell.Direction.WEST, new Cell(row, -1, Cell.Type.UNKNOWN));

				// West
				if (col < cols - 1) {
					if (linkCells(cell, other = cells[row][col + 1]))
						cell.setNeighbor(Cell.Direction.EAST, other);
				} else if (cell.getType() != Cell.Type.HALL) cell.setNeighbor(Cell.Direction.EAST, new Cell(row, row + 1, Cell.Type.UNKNOWN));
			}
		}

		// Calculate doors for each room.
		rooms.values().forEach(Room::calculateDoorSteps);
	}

	public boolean linkCells(Cell a, Cell b) {
		return (a.getType() == b.getType());
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getSprites: Return a map of all the Characters on the Game.Board.
	 * (Where keys are their corresponding SpriteAlias).
	 *
	 * @return map of Characters on the Game.Board.
	 */
	public Map<Sprite.SpriteAlias, Sprite> getSprites() {
		return sprites;
	}

	/**
	 * getRooms: Return a map of all the Rooms on the Game.Board.
	 * (Where keys are their corresponding RoomAlias).
	 *
	 * @return map of Rooms on the Game.Board.
	 */
	public Map<Room.RoomAlias, Room> getRooms() {
		return rooms;
	}

	/**
	 * getWeapons: Return a map of all the Weapons in the Game.Board.
	 * (Where keys are their corresponding WeaponAlias).
	 *
	 * @return map of Weapons on the Game.Board.
	 */
	public Map<Weapon.WeaponAlias, Weapon> getWeapons() {
		return weapons;
	}

	public int getRows() { return rows; }
	public int getCols() { return cols; }


	/**
	 * moveUser: Move a Game.User to another Game.Cell.
	 * @param user Game.User to move.
	 * @param target Game.Cell to move to.
	 */
	public void moveUser(User user, Cell target) {
		if (target == null || user == null)
			throw new InvalidParameterException("Cannot pass a null parameter!");

		if (target.getSprite() != null)
			throw new RuntimeException("Game.Position is already taken!");

		Cell from = user.getSprite().getPosition();

		Sprite sprite = user.getSprite();
		sprite.setPosition(target);

		target.setSprite(sprite);
		from.setSprite(null);
	}

	/**
	 * getCell: Return the Game.Cell stored at the given row/col.
	 * @param row Row of the cell to return.
	 * @param col Col of the cell to return.
	 * @return Game.Cell at the provided row/col.
	 */
	public Cell getCell(int row, int col) {
		if ((row < 0 || row >= rows - 1) || (col < 0 || col >= cols - 1))
			return null;

		return cells[row][col];
	}

	/**
	 * getCell: Return the Game.Cell stored at the given row/col.
	 * Throws InvalidParameterException.
	 * @param cord String of coordinate. Ie, H13
	 * @return Game.Cell at cord.
	 */
	public Cell getCell(String cord) {

		int col, row;
		try {
			col = Character.toUpperCase(cord.charAt(0)) - 'A';
			row = Integer.parseUnsignedInt(cord.substring(1)) - 1;
		} catch (Exception e) { throw new InvalidParameterException(); }

		return getCell(row, col);
	}

	/**
	 * printBoardState: Prints the current state of the board to the console.
	 */
	public void printBoardState() {
		System.out.println("\t\t   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
		for (int row = 0; row < rows; row++) {
			System.out.print("\t\t");
			for (int col = 0; col < cols; col++) {
				// Print the row number at the start of each line
				if (col == 0) {
					String factoredRowNum = String.format("%02d", (row+1));
					System.out.print(factoredRowNum + "|");
				}

				System.out.print(cells[row][col].toString());
				System.out.print("|");
			}
			// New line for every row
			System.out.print("\n");
		}
		System.out.println("\t\t   A B C D E F G H I J K L M N O P Q R S T U V W X\n");
	}

	/**
	 * getMapBase: Returns a String copy of the map to load.
	 * @return String copy of map to load.
	 */
	private String getMapBase() {
		return "MAP 25 24\r\n" +
				"   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\r\n" +
				"01|#|#|#|#|#|#|#|0|#|#|#|#|#|#|#|#|1|#|#|#|#|#|#|#|\r\n" +
				"02|#|K|K|K|K|#|_|_|#|#|#|B|B|#|#|#|_|_|#|C|C|C|C|#|\r\n" +
				"03|#|K|K|K|K|#|_|_|#|B|B|B|B|B|B|#|_|_|#|C|C|C|C|#|\r\n" +
				"04|#|K|K|K|K|#|_|_|#|B|B|B|B|B|B|#|_|_|#|C|C|C|C|#|\r\n" +
				"05|#|K|K|K|K|#|_|_|#|B|B|B|B|B|B|#|_|_|#|C|#|C|C|#|\r\n" +
				"06|#|#|#|#|K|#|_|_|B|B|B|B|B|B|B|B|_|_|_|_|#|#|#|#|\r\n" +
				"07|#|_|_|_|_|_|_|_|#|#|B|#|#|B|#|#|_|_|_|_|_|_|_|2|\r\n" +
				"08|#|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|#|\r\n" +
				"09|#|#|#|#|_|_|_|_|_|_|_|_|_|_|_|_|_|_|#|#|#|#|#|#|\r\n" +
				"10|#|D|D|#|#|#|#|_|_|#|#|#|#|#|#|_|_|_|#|A|A|A|A|#|\r\n" +
				"11|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|A|A|A|A|A|#|\r\n" +
				"12|#|D|D|D|D|D|D|_|_|#|#|#|#|#|#|_|_|_|#|A|A|A|A|#|\r\n" +
				"13|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|#|#|#|#|A|#|\r\n" +
				"14|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|_|_|_|_|_|#|\r\n" +
				"15|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|#|#|L|#|#|#|\r\n" +
				"16|#|#|#|#|D|#|#|_|_|#|#|#|#|#|#|_|_|#|#|L|L|L|L|#|\r\n" +
				"17|#|_|_|_|_|_|_|_|_|#|#|#|#|#|#|_|_|L|L|L|L|L|L|#|\r\n" +
				"18|5|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|#|#|L|L|L|L|#|\r\n" +
				"19|#|_|_|_|_|_|_|_|#|#|H|H|H|#|#|_|_|_|#|#|#|#|#|#|\r\n" +
				"20|#|#|#|#|E|#|_|_|#|H|H|H|H|H|#|_|_|_|_|_|_|_|_|3|\r\n" +
				"21|#|E|E|E|E|#|_|_|#|H|H|H|H|H|H|_|_|_|_|_|_|_|_|#|\r\n" +
				"22|#|E|E|E|E|#|_|_|#|H|H|H|H|H|#|_|_|#|S|#|#|#|#|#|\r\n" +
				"23|#|E|E|E|E|#|_|_|#|H|H|H|H|H|#|_|_|#|S|S|S|S|S|#|\r\n" +
				"24|#|E|E|E|E|#|_|_|#|H|H|H|H|H|#|_|_|#|S|S|S|S|S|#|\r\n" +
				"25|#|#|#|#|#|#|4|#|#|#|#|#|#|#|#|#|#|#|#|#|#|#|#|#|\r\n" +
				"   A B C D E F G H I J K L M N O P Q R S T U V W X";
	}

	public Stream<Cell> getStream() {
		List<Cell> cellList = new ArrayList<>();
		for (int r=0; r!=rows; ++r) {
			for (int  c=0; c!=cols; ++c) {
				cellList.add(cells[r][c]);
			}
		}
		return cellList.stream();
	}
}