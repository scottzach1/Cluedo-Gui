package tests;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class cellTests {

	/**
	 * Tests whether a Cell.Type is parsed correctly from a
	 * capital letter char.
	 *
	 * @param c    letter.
	 * @param type of Cell.
	 */
	private void testCellGetType(char c, Cell.Type type) {
		assertEquals(type, Cell.getType(Character.toUpperCase(c)));
		assertEquals(Cell.Type.UNKNOWN, Cell.getType(Character.toLowerCase(c)));
	}

	/**
	 * Checks whether Cellar Cell are parsed correctly.
	 */
	@Test
	void testParseCellar() {
		testCellGetType('M', Cell.Type.CELLAR);
	}

	/**
	 * Checks whether Weapon Cell are parsed correctly.
	 */
	@Test
	void testParseWeapon() {
		testCellGetType('W', Cell.Type.WEAPON);
	}

	@Test
	void testParseVoid() {
		assertEquals(Cell.Type.VOID, Cell.getType(' '));
	}

	/**
	 * Checks whether Doorway Cells are parsed correctly.
	 */
	@Test
	void testParseDoorways() {
		testCellGetType('X', Cell.Type.ROOM);
		testCellGetType('Y', Cell.Type.HALL);
	}

	/**
	 * Checks whether Rooms are parsed correctly.
	 */
	@Test
	void testParseRoomPattern() {
		Set<Character> reserved = new HashSet<>(
						Arrays.asList('M', 'W', 'X', 'Y')
		);

		for (char c = 'A'; c <= 'Z'; ++c) {
			if (reserved.contains(c)) continue;
			testCellGetType(c, Cell.Type.ROOM);
		}
	}

	/**
	 * Checks whether Startpads are parsed correctly.
	 */
	@Test
	void testParseStartPad() {
		for (int i = 0; i <= 9; ++i) {
			char c = (i + "").charAt(0);
			assertEquals(Cell.Type.START_PAD, Cell.getType(c));
		}
	}


	/**
	 * Checks Cell.sameCell() method to determine whether Cells
	 * are treated as the same cell.
	 */
	@Test
	void testSameCell() {
		Board board = new Board(null);

		Room ballRoom = new Room(Room.RoomAlias.BALLROOM);
		Room diningRoom = new Room(Room.RoomAlias.DINING_ROOM);

		Cell ball = new Cell(0, 0, Cell.Type.ROOM, board);
		ball.setRoom(ballRoom);

		Cell otherBall = new Cell(0, 0, Cell.Type.ROOM, board);
		otherBall.setRoom(ballRoom);

		Cell hall = new Cell(0, 0, Cell.Type.HALL, board);
		Cell otherHall = new Cell(0, 0, Cell.Type.HALL, board);

		Cell dining = new Cell(0, 0, Cell.Type.ROOM, board);
		dining.setRoom(diningRoom);


		assertTrue(ball.sameCell(ball));
		assertTrue(ball.sameCell(otherBall));

		assertFalse(ball.sameCell(hall));
		assertFalse(ball.sameCell(dining));

		assertFalse(hall.sameCell(otherHall));
		// Major difference from Cell.sameRoom()
		assertTrue(hall.sameCell(hall));
	}


	/**
	 * Checks Cell.sameRoom() method to determine whether Cells
	 * are treated as the same room.
	 */
	@Test
	void testSameRoom() {
		Board board = new Board(null);

		Room ballRoom = new Room(Room.RoomAlias.BALLROOM);
		Room diningRoom = new Room(Room.RoomAlias.DINING_ROOM);

		Cell ball = new Cell(0, 0, Cell.Type.ROOM, board);
		ball.setRoom(ballRoom);

		Cell otherBall = new Cell(0, 0, Cell.Type.ROOM, board);
		otherBall.setRoom(ballRoom);

		Cell hall = new Cell(0, 0, Cell.Type.HALL, board);
		Cell otherHall = new Cell(0, 0, Cell.Type.HALL, board);

		Cell dining = new Cell(0, 0, Cell.Type.ROOM, board);
		dining.setRoom(diningRoom);


		assertTrue(ball.sameRoom(ball));
		assertTrue(ball.sameRoom(otherBall));

		assertFalse(ball.sameRoom(hall));
		assertFalse(ball.sameRoom(dining));

		assertFalse(hall.sameRoom(otherHall));
		// Major difference from Cell.sameCell()
		assertFalse(hall.sameRoom(hall));
	}

	/**
	 * Checks Cell outputs correct String coordinates.
	 */
	@Test
	void getStringCords() {
		Board board = new Board(null);
		Cell cell;

		cell = new Cell(0, 0, Cell.Type.HALL, board);
		assertEquals("A1", cell.getStringCoordinates());

		cell = new Cell(5, 5, Cell.Type.HALL, board);
		assertEquals("F6", cell.getStringCoordinates());

		cell = new Cell(23, 23, Cell.Type.ROOM, board);
		assertEquals("X24", cell.getStringCoordinates());

		cell = new Cell(25, 25, Cell.Type.VOID, board);
		assertEquals("Z26", cell.getStringCoordinates());
	}

	/**
	 * Tests Cell.go() method.
	 * Creates a small array and checks neighbours of all cells
	 * in all directions.
	 */
	@Test
	void testGo() {
		Board board = new Board(null);
		Cell[][] cells = new Cell[3][3];

		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col] = new Cell(row, col, Cell.Type.HALL, board);
			}
		}

		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				Cell cell = cells[row][col];
				for (Cell.Direction direction : Cell.Direction.values()) {
					Cell neighbour = null;
					try {
						switch (direction) {
							case EAST:
								neighbour = cells[row][col + 1];
								break;
							case WEST:
								neighbour = cells[row][col - 1];
								break;
							case NORTH:
								neighbour = cells[row - 1][col];
								break;
							case SOUTH:
								neighbour = cells[row + 1][col];
								break;
						}
					} catch (IndexOutOfBoundsException e) {
						neighbour = null;
					}

					assertEquals(neighbour, cell.go(cells, direction));
				}
			}
		}
	}

	/**
	 * Tests setter and getter for room.
	 */
	@Test void testRoom() {
		Board board = new Board(null);

		Cell cell = new Cell(0,0, Cell.Type.ROOM, board);
		Room room = new Room(Room.RoomAlias.HALL);

		assertFalse(cell.hasRoom());
		assertNull(cell.getRoom());

		cell.setRoom(room);

		assertTrue(cell.hasRoom());
		assertEquals(room, cell.getRoom());
	}

	/**
	 * Tests getter for weapon.
	 * (Pulls from Room for Weapon).
	 */
	@Test void testWeapon() {
		Board board = new Board(null);

		Cell cell = new Cell(0,0, Cell.Type.WEAPON, board);
		Room room = new Room(Room.RoomAlias.HALL);
		Weapon weapon = new Weapon(Weapon.WeaponAlias.DAGGER);
		room.setWeapon(weapon);

		assertFalse(cell.hasWeapon());
		assertNull(cell.getWeapon());

		cell.setRoom(room);

		assertTrue(cell.hasWeapon());
		assertEquals(weapon, cell.getWeapon());
	}

	/**
	 * Tests setter and getters for Sprite.
	 * Also tests whether a cell is free.
	 */
	@Test void testSprite() {
		Board board = new Board(null);

		Cell cell = new Cell(0, 0, Cell.Type.ROOM, board);
		Sprite sprite = new Sprite(Sprite.SpriteAlias.COLONEL_MUSTARD);

		assertNull(cell.getSprite());
		assertTrue(cell.missingRoom());

		cell.setSprite(sprite);

		assertEquals(sprite, cell.getSprite());
		assertFalse(cell.isFree());

		cell.setRoom(new Room(Room.RoomAlias.BILLARD_ROOM));

		assertEquals(sprite, cell.getSprite());
		assertTrue(cell.isFree());
	}

	/**
	 * Tests setting and getting Neighbours stored in the
	 * HashMap.
	 */
	@Test void testGetNeighbour() {
		Board board = new Board(null);

		Cell cell = new Cell(1, 1, Cell.Type.HALL, board);

		for (Cell.Direction direction : Cell.Direction.values()) {
			assertNull(cell.getNeighbors().get(direction));
			Cell neighbour = new Cell(0, 0, Cell.Type.HALL, board);
			cell.setNeighbor(direction, neighbour);
			assertEquals(neighbour, cell.getNeighbors().get(direction));
		}
	}

}
