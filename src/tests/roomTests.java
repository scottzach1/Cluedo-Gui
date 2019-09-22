package tests;

import game.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class roomTests {

	/**
	 * Tests cells getter.
	 */
	@Test void testGetCells() {
		Board board = new Board(null);
		Room room = new Room(Room.RoomAlias.KITCHEN);

		for (int i=0; i<15; ++i) {
			assertEquals(i, room.getCells().size());
			room.getCells().add(new Cell(0, 0, Cell.Type.ROOM, board));
		}

	}

	/**
	 * Tests roomAlias getter.
	 */
	@Test void testRoomAlias() {
		for (Room.RoomAlias roomAlias : Room.RoomAlias.values()) {
			Room room = new Room(roomAlias);
			assertEquals(room.getRoomAlias(), roomAlias);
		}
	}

	/**
	 * Tests weapon setter and getter.
	 */
	@Test void testGetWeapon() {
		Room room = new Room(Room.RoomAlias.STUDY);

		for (Weapon.WeaponAlias weaponAlias : Weapon.WeaponAlias.values()) {
			Weapon weapon = new Weapon(weaponAlias);
			room.setWeapon(weapon);
			assertEquals(weapon, room.getWeapon());
		}
	}

	/**
	 * Tests doorsteps are calculated correctly.
	 */
	@Test void testDoorSteps() {
		Board board = new Board(null);
		Room room = new Room(Room.RoomAlias.LIBRARY);

		Cell inRoom = new Cell(0,0, Cell.Type.ROOM, board);
		Cell door = new Cell(1,0, Cell.Type.HALL, board);

		assertEquals(0, room.getDoorSteps().size());

		room.getCells().add(inRoom);
		inRoom.setNeighbor(Cell.Direction.SOUTH, door);
		room.calculateDoorSteps();

		assertEquals(1, room.getDoorSteps().size());
	}

	/**
	 * Tests sprites are calculated in this room.
	 */
	@Test void testGetInThisRoom() {
		Board board = new Board(null);
		Room room = new Room(Room.RoomAlias.LOUNGE);
		Cell cell1 = new Cell(0, 0, Cell.Type.ROOM, board);
		Cell cell2 = new Cell(1,0, Cell.Type.ROOM, board);

		Sprite sprite1 = new Sprite(Sprite.SpriteAlias.COLONEL_MUSTARD);
		Sprite sprite2 = new Sprite(Sprite.SpriteAlias.PROFESSOR_PLUM);

		room.getCells().add(cell1);
		room.getCells().add(cell2);

		assertEquals(0, room.getInThisRoom().size());
		cell1.setSprite(sprite1);
		assertEquals(1, room.getInThisRoom().size());
		cell2.setSprite(sprite2);
		assertEquals(2, room.getInThisRoom().size());
		cell1.setSprite(sprite2);
		assertEquals(1, room.getInThisRoom().size());
	}

	/**
	 * Tests roomAlias is parsed correctly from int.
	 */
	@Test void parseAliasFromInt() {
		Room.RoomAlias[] roomAliases = Room.RoomAlias.values();

		for (int i=0; i<roomAliases.length; ++i) {
			assertEquals(roomAliases[i], Room.parseAliasFromOrdinalInt(i));
		}

		try {
			Room.parseAliasFromOrdinalInt(roomAliases.length);
			fail();
		} catch (Exception e) {
		}
	}

	/**
	 * Tests toString() is consistent.
	 */
	@Test void testToString() {
		for (Room.RoomAlias roomAlias : Room.RoomAlias.values()) {
			Room room = new Room(roomAlias);

			assertEquals(roomAlias.toString(), room.toString());
		}
	}

}
