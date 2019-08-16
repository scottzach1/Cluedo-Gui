package Tests;

import Game.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LUITests {

    /**
     * Tests.Tests edge case distance for path finding.
     *
     * @param b          board
     * @param startSting string cord of start cell
     * @param endString  string cord of end cell
     * @param realDist   the true distance expected on map.
     */
    private void testShortestPath(Board b, String startSting, String endString, int realDist) {
        Cell start = b.getCell(startSting);
        Cell end = b.getCell(endString);

        PathFinder pathFinder;

        pathFinder = new PathFinder(b);
        assertEquals(realDist, pathFinder.findShortestPath(start, end));
        pathFinder = new PathFinder(b);
        assertEquals(realDist, pathFinder.findShortestPath(end, start));

        pathFinder = new PathFinder(b);
        assertEquals(realDist, pathFinder.findShortestPathFromString(startSting, endString));
        pathFinder = new PathFinder(b);
        assertEquals(realDist, pathFinder.findShortestPathFromString(endString, startSting));
    }

    @Test void testPathFinding() {
        // Test generic paths.
        Board b = new Board();
        PathFinder pathFinder;

        testShortestPath(b, "H2", "H4", 2);
        testShortestPath(b, "H2", "G7", 6);
        testShortestPath(b, "B2","H2", 9);
        testShortestPath(b, "Q21","B24", 11);
        testShortestPath(b, "H2", "H7", 5);

        // End is a wall
        assertEquals(Cell.Type.WALL, b.getCell("F2").getType());
        testShortestPath(b, "H1", "F2", Integer.MAX_VALUE);

        // End holds a character
        assertNotNull(b.getCell("H1").getSprite());
        assertNull(b.getCell("H5").getSprite());

        pathFinder = new PathFinder(b);
        assertEquals(Integer.MAX_VALUE, pathFinder.findShortestPathFromString("H5", "H1"));

        pathFinder = new PathFinder(b, new HashSet<>(), new HashSet<>(Collections.singleton(b.getCell("C3").getRoom())));
        assertEquals(Integer.MAX_VALUE, pathFinder.findShortestPathFromString("H5", "C3"));

        // Test Basic Path Finding
        pathFinder = new PathFinder(b);
        assertTrue(pathFinder.findExactPathFromString("H2", "H7", 15));
        pathFinder = new PathFinder(b);
        assertFalse(pathFinder.findExactPathFromString("H2", "H7", 3));
        pathFinder = new PathFinder(b);
        assertTrue(pathFinder.findExactPathFromString("H2", "H7", 7));

        pathFinder = new PathFinder(b);
        assertTrue(pathFinder.findExactPathFromString("P21", "J18", 3));
        pathFinder = new PathFinder(b);
        assertFalse(pathFinder.findExactPathFromString("P21", "J18", 7));
        pathFinder = new PathFinder(b);
        assertTrue(pathFinder.findExactPathFromString("P21", "J18", 5));
        pathFinder = new PathFinder(b);
        assertTrue(pathFinder.findExactPathFromString("P21", "J18", 4));
        pathFinder = new PathFinder(b);
        assertTrue(pathFinder.findExactPathFromString("P21", "J18", 9));

        pathFinder = new PathFinder(b);
        assertEquals(7, pathFinder.findShortestPathFromString("I8", "P8"));
        // Test Visited Cells
        assertEquals(6, pathFinder.findShortestPathFromString("J9", "K7"));
        // Test Visited Rooms
        assertTrue(30 < pathFinder.findShortestPathFromString("H6", "Q6"));

        pathFinder = new PathFinder(b);
        assertEquals(7, pathFinder.findShortestPathFromString("I8", "P8"));
        // Test Visited Cells
        assertTrue(pathFinder.findExactPathFromString("J9", "K7", 6));
        // Test Visited Rooms
        assertFalse(pathFinder.findExactPathFromString("H6", "Q6", 3));

    }

    @Test void testBoard() {
        // Check maps are populated.
        Board b = new Board();

        for (Room.RoomAlias roomAlias : Room.RoomAlias.values()) {
            assertEquals(roomAlias, b.getRooms().get(roomAlias).getRoomAlias());
        }

        for (Sprite.SpriteAlias spriteAlias : Sprite.SpriteAlias.values()) {
            assertEquals(spriteAlias, b.getSprites().get(spriteAlias).getSpriteAlias());
        }

        for (Weapon.WeaponAlias weaponAlias : Weapon.WeaponAlias.values()) {
            assertEquals(weaponAlias, b.getWeapons().get(weaponAlias).getWeaponAlias());
        }

        // Checking getCell and moveUser
        User user = new User();
        assertNull(user.getSprite());

        user.setSprite(b.getSprites().get(Sprite.SpriteAlias.MRS_WHITE));
        assertEquals(b.getSprites().get(Sprite.SpriteAlias.MRS_WHITE),user.getSprite());

        assertNull(b.getCell("H3").getSprite());
        assertEquals(user.getSprite(), b.getCell("H1").getSprite());

        b.moveUser(user, b.getCell("H3"));

        assertNull(b.getCell("H1").getSprite());
        assertEquals(user.getSprite(), b.getCell("H3").getSprite());

        try {
            b.moveUser(user, b.getCell("H3"));
            fail("Invalid Move didn't throw Exception");
        } catch (Exception e) {}

        try {
            b.moveUser(null, b.getCell("H3"));
            fail("Invalid Move didn't throw Exception");
        } catch (Exception e) {}

    }

    @Test void testCellToString() {
        Cell cell = new Cell(5, 5, Cell.Type.WALL);
        assertEquals(cell.getStringCoordinates(), "F6");

        cell = new Cell(5, 5, Cell.Type.WALL);
        assertEquals("#", cell.toString());

        cell = new Cell(5, 5, Cell.Type.BLANK);
        assertEquals("_", cell.toString());

        cell = new Cell(5, 5, Cell.Type.ROOM);
        assertEquals("_", cell.toString());

        cell = new Cell(5, 5, Cell.Type.START_PAD);
        assertEquals("$", cell.toString());
    }

    @Test void testRoom() {
        // Checking doors generated.
        Board b = new Board();

        assertEquals(1, b.getRooms().get(Room.RoomAlias.KITCHEN).getDoors().size());
        assertEquals(4, b.getRooms().get(Room.RoomAlias.BALLROOM).getDoors().size());
        assertEquals(1, b.getRooms().get(Room.RoomAlias.CONSERVATORY).getDoors().size());
        assertEquals(2, b.getRooms().get(Room.RoomAlias.LIBRARY).getDoors().size());
        assertEquals(1, b.getRooms().get(Room.RoomAlias.STUDY).getDoors().size());
        assertEquals(4, b.getRooms().get(Room.RoomAlias.HALL).getDoors().size());
        assertEquals(1, b.getRooms().get(Room.RoomAlias.LOUNGE).getDoors().size());
        assertEquals(2, b.getRooms().get(Room.RoomAlias.DINING_ROOM).getDoors().size());

        // Checking parsingEnum strings.
        Queue<String> roomAliasStrings = new ArrayDeque<>(Arrays.asList("KITCHEN", "BALLROOM", "CONSERVATORY", "BILLARD_ROOM", "DINING_ROOM", "LIBRARY", "LOUNGE", "HALL", "STUDY"));
        Queue<Character> roomCharList = new ArrayDeque<>(Arrays.asList('K', 'B', 'C', 'A', 'D', 'L', 'E', 'H', 'S'));
        for (int i = 0; i < Room.RoomAlias.values().length; i++) {
            assertEquals(roomAliasStrings.peek(), Room.parseAliasFromOrdinalInt(i).toString());
            assertEquals(roomAliasStrings.poll(), Room.parseAliasFromChar(roomCharList.poll()).toString());
        }

        User user1 = new User(), user2 = new User();

        user1.setSprite(b.getSprites().get(Sprite.SpriteAlias.MRS_WHITE));
        user2.setSprite(b.getSprites().get(Sprite.SpriteAlias.COLONEL_MUSTARD));

        // Testing Moving Game.User.
        b.moveUser(user1, b.getCell("B24"));
        b.moveUser(user2, b.getCell("E20"));

        Set<Sprite> expectedSprites = new HashSet<>(Arrays.asList(user1.getSprite(), user2.getSprite()));
        Set<Sprite> recordedSprites = b.getRooms().get(Room.RoomAlias.LOUNGE).getInThisRoom();

        assertEquals(expectedSprites.size(), recordedSprites.size());
        assertTrue(recordedSprites.containsAll(expectedSprites));

        // Checking Game.Room sizes.
        assertEquals(17, b.getRooms().get(Room.RoomAlias.KITCHEN).getCells().size());
        assertEquals(30, b.getRooms().get(Room.RoomAlias.BALLROOM).getCells().size());
        assertEquals(15, b.getRooms().get(Room.RoomAlias.CONSERVATORY).getCells().size());
        assertEquals(14, b.getRooms().get(Room.RoomAlias.BILLARD_ROOM).getCells().size());
        assertEquals(15, b.getRooms().get(Room.RoomAlias.LIBRARY).getCells().size());
        assertEquals(11, b.getRooms().get(Room.RoomAlias.STUDY).getCells().size());
        assertEquals(29, b.getRooms().get(Room.RoomAlias.HALL).getCells().size());
        assertEquals(17, b.getRooms().get(Room.RoomAlias.LOUNGE).getCells().size());
        assertEquals(29, b.getRooms().get(Room.RoomAlias.DINING_ROOM).getCells().size());

    }

    @Test void testSpriteAndUser() {
        // Check  2 way setter getters
        User.resetUserNoCounter();
        Board b = new Board();
        User user = new User();

        for (Sprite sprite : b.getSprites().values()) {
            sprite.setUser(user);
            assertEquals(sprite, user.getSprite());
            assertEquals(user, sprite.getUser());
        }

        for (Sprite sprite : b.getSprites().values()) {
            user.setSprite(sprite);
            assertEquals(sprite, user.getSprite());
            assertEquals(user, sprite.getUser());
        }

        // Check toStrings
        for (Sprite.SpriteAlias spriteAlias : Sprite.SpriteAlias.values()) {
            Sprite sprite = b.getSprites().get(spriteAlias);
            switch (sprite.getSpriteAlias()) {
                case MRS_WHITE:
                    assertEquals("W", sprite.toString());
                    break;
                case MR_GREEN:
                    assertEquals("G", sprite.toString());
                    break;
                case MRS_PEACOCK:
                    assertEquals("Q", sprite.toString());
                    break;
                case PROFESSOR_PLUM:
                    assertEquals("P", sprite.toString());
                    break;
                case MISS_SCARLETT:
                    assertEquals("S", sprite.toString());
                    break;
                case COLONEL_MUSTARD:
                    assertEquals("M", sprite.toString());
                    break;
            }
        }


        // Checking parsing.
        Queue<String> spriteAliasStrings = new ArrayDeque<>(Arrays.asList("MRS_WHITE", "MR_GREEN", "MRS_PEACOCK", "PROFESSOR_PLUM", "MISS_SCARLETT", "COLONEL_MUSTARD"));

        for (int i = 0; i < Sprite.SpriteAlias.values().length; i++) {
            assertEquals(spriteAliasStrings.peek(), Sprite.parseAliasFromOrdinalInt(i).toString());
            assertEquals(spriteAliasStrings.poll(), Sprite.parseAliasFromOrdinalChar((i + "").charAt(0)).toString());
        }
    }

    @Test void testWeapon() {
        // Check setters and getters.

        Board b = new Board();
        Weapon w1 = b.getWeapons().get(Weapon.WeaponAlias.CANDLE_STICK);
        Room r = w1.getRoom();

        Weapon w2 = new Weapon(Weapon.WeaponAlias.DAGGER);

        assertEquals(w1, r.getWeapon());
        assertEquals(r, w1.getRoom());

        w1.setRoom(b.getRooms().get(Room.RoomAlias.KITCHEN));
        w2.setRoom(r);

        assertEquals(w2, r.getWeapon());
        assertEquals(r, w2.getRoom());

        assertNotEquals(w1, w2);

        // Checking parsing.
        Queue<String> weaponAliasStrings = new ArrayDeque<>(Arrays.asList("CANDLE_STICK", "DAGGER", "LEAD_PIPE", "REVOLVER", "ROPE", "SPANNER"));

        for (int i = 0; i < Weapon.WeaponAlias.values().length; i++) {
            assertEquals(weaponAliasStrings.poll(), Weapon.parseAliasFromOrdinalInt(i).toString());
        }
    }

    @Test void testUser() {
        // Checking userNo enum.
        User.resetUserNoCounter();

        User user = null;
        for (int i=1; i<User.userNo.values().length; i++) {
            user = new User();
            user.setUserName("Meep" + i);
            assertEquals(i, User.getUserNoCounter());
            assertEquals(i, user.getUserNo().ordinal());
            assertEquals("Meep" + i, user.getUserName());
        }

        // Checking hand.
        Card card = new Room(Room.RoomAlias.KITCHEN);
        assertNotNull(user);
        assertEquals(0, user.getHand().size());
        user.addToHand(new Weapon(Weapon.WeaponAlias.DAGGER));
        user.addToHand(card);
        assertEquals(2, user.getHand().size());
        assertTrue(user.getHand().contains(card));
    }

}