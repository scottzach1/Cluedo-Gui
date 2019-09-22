package tests;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class boardTests {

	/**
	 * Checks all rooms are loaded correctly into the map.
	 */
	@Test
	void testRooms() {
		Board board = new Board(null);

		Map<Room.RoomAlias, Room> roomMap = board.getRooms();

		assertEquals(Room.RoomAlias.values().length, roomMap.size());

		for (Room.RoomAlias roomAlias : Room.RoomAlias.values()) {
			assertEquals(roomAlias, roomMap.get(roomAlias).getRoomAlias());
		}

	}

	/**
	 * Checks all weapons are loaded correctly into the map.
	 */
	@Test void testWeapons() {
		Board board = new Board(null);

		Map<Weapon.WeaponAlias, Weapon> weaponMap = board.getWeapons();

		assertEquals(Weapon.WeaponAlias.values().length, weaponMap.size());

		for (Weapon.WeaponAlias weaponAlias : Weapon.WeaponAlias.values()) {
			assertEquals(weaponAlias, weaponMap.get(weaponAlias).getWeaponAlias());
		}
	}

	/**
	 * Checks all sprites are loaded correctly into the map.
	 */
	@Test void testSprites() {
		Board board = new Board(null);

		Map<Sprite.SpriteAlias, Sprite> spriteMap = board.getSprites();

		assertEquals(Sprite.SpriteAlias.values().length, spriteMap.size());

		for (Sprite.SpriteAlias spriteAlias : Sprite.SpriteAlias.values()) {
			assertEquals(spriteAlias, spriteMap.get(spriteAlias).getSpriteAlias());
		}
	}

	/**
	 * Checks getCell methods are consistent.
	 */
	@Test
	void testGetCell() {
		Board board = new Board(null);

		for (int row = 0; row < board.getRows() - 1; row++) {
			for (int col = 0; col < board.getCols() - 1; col++) {
				char c = (char) ('A' + col);
				assertEquals(c + "" + (row + 1), board.getCell(row, col).getStringCoordinates());
			}
		}
	}
}
