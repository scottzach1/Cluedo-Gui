package tests;

import game.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class spriteTests {

	/**
	 * Tests User setter and getter.
	 */
	@Test void testUser() {
		Sprite sprite = new Sprite(Sprite.SpriteAlias.PROFESSOR_PLUM);
		User user = new User();

		assertNull(sprite.getUser());
		sprite.setUser(user);
		assertEquals(user, sprite.getUser());
	}

	/**
	 * Tests sprite getter.
	 */
	@Test void testAlias() {
		for (Sprite.SpriteAlias spriteAlias : Sprite.SpriteAlias.values()) {
			Sprite sprite = new Sprite(spriteAlias);
			assertEquals(spriteAlias, sprite.getSpriteAlias());
			for (Sprite.SpriteAlias otherAlias : Sprite.SpriteAlias.values()) {
				assertEquals((spriteAlias == otherAlias), sprite.matchesType(otherAlias));
			}
		}
	}

	/**
	 * Tests cell position setter and getter.
	 */
	@Test void testPosition() {
		Board board = new Board(null);
		Cell oldCell = new Cell(0, 0, Cell.Type.ROOM, board);
		Cell newCell = new Cell(5,5, Cell.Type.HALL, board);

		Sprite sprite = new Sprite(Sprite.SpriteAlias.MR_GREEN);
		assertNull(sprite.getPosition());

		sprite.setPosition(oldCell);
		assertEquals(oldCell, sprite.getPosition());

		sprite.setPosition(newCell);
		assertEquals(newCell, sprite.getPosition());
	}

	/**
	 * Tests toString method is consistent.
	 */
	@Test void testToString() {
		for (Sprite.SpriteAlias spriteAlias : Sprite.SpriteAlias.values()) {
			Sprite sprite = new Sprite(spriteAlias);

			assertEquals(spriteAlias.toString(), sprite.toString());
		}
	}
}
