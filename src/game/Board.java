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
    private int prevCellSize;

    static Map<String, ImageIcon> baseImageIcons = new HashMap<>();
    static Map<String, ImageIcon> scaledImageIcons = new HashMap<>();
    PathFinder pathFinder;

    private Cell[][] cells;
    private int rows, cols;

    CluedoGame cluedoGame;
    Set<Cell> visitedCells, highlightedCells;
    Set<Room> visitedRooms, highlightedRooms;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    /**
     * Game.Board: The constructor for Game.Board.
     * - Creates a new 2d array and populates with Cells
     * - Initialises ImageIcons for Cells
     * - Generates sprites and places at corresponding starting positions.
     * - Generates Weapons and randomly allocates them to Rooms.
     * @param cluedoGame game
     */
    public Board(CluedoGame cluedoGame) {
        this.cluedoGame = cluedoGame;

        visitedCells = new HashSet<>();
        visitedRooms = new HashSet<>();
        highlightedCells = new HashSet<>();
        highlightedRooms = new HashSet<>();

        pathFinder = new PathFinder(this);
        setupImageIcons();
        generateComponents();
        setupCells();
    }

    /**
     * Generates Integral Components:
     * - Rooms
     * - Weapons
     * - Sprites
     */
    private void generateComponents() {
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
    }

    /**
     * Renders all cells, and the dice.
     */
    public void render() {
        getStream().forEach(Cell::render);
        cluedoGame.getGui().redraw();
    }

    /**
     * Generates Board from Map.txt.
     * - Parses file
     * - Generates Cells
     * - Links Cells
     */
    private void setupCells() {
        // This are using for linking the door ways later.
        Set<Cell> doorSteps = new HashSet<>();

        /*
         * Load Board From File:
         */

        try {

            Scanner sc = new Scanner(new File("map.txt"));

            if (!sc.next().equals("MAP")) throw new Exception("Invalid File Type");

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
                    Cell cell = new Cell(row, col, (type != Cell.Type.START_PAD) ? type : Cell.Type.HALL, this);

                    cells[row][col] = cell;

                    if (c == 'X') continue; // This is a door, ignore for now.
                    if (c == 'Y') doorSteps.add(cell); // This is a doorstep. Use later.

                    if (type == Cell.Type.START_PAD) {
                    	// Assign Sprite
                        try {
                            Sprite currentSprite = sprites.get(Sprite.parseAliasFromOrdinalChar(c));
                            currentSprite.setPosition(cell);
                            cell.setSprite(currentSprite);
                        } catch (Exception e) {
                            System.out.println("Not a number cell!");
                            // Continue as normal as if it were blank.
                        }
                    } else if (type == Cell.Type.ROOM) {
						// Assign Room
                        Room room = rooms.get(Room.parseAliasFromChar(c));
                        room.addCell(cell);
                        cell.setRoom(room);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("File Exception: " + e);
        }

        // Link Rooms & Doors
        getStream().forEach(cell -> {
            if (!cell.missingRoom()) return; // Skip cell.

            for (Cell.Direction dir : Cell.Direction.values()) {
                Cell other = cell.go(cells, dir);

                if (other == null) continue; // Edge of map

				// Found Neigh w/ Room:
                if (other.isType(Cell.Type.ROOM) && other.hasRoom())
                    cell.setRoom(other.getRoom());
                // Found Doorstep w/o Room:
                else if (other.isType(Cell.Type.HALL) && doorSteps.contains(other)) {
                    cell.setNeighbor(dir, other);
                    other.setNeighbor(dir.reverse(), cell);
                }
            }
        });

        // Link Cells w/ Neighbours
        getStream().forEach(cell -> {
            for (Cell.Direction dir : Cell.Direction.values()) {
                Cell other = cell.go(cells, dir);

                // If valid neigh, link Cell to, (and not Other from)
                if (other != null) {
                    if (linkCells(cell, other)) {
                        cell.setNeighbor(dir, other);
                    } // Link ede of map w/ invisible cells.
                } else if (cell.isType(Cell.Type.VOID)) {
                    cell.setNeighbor(dir, new Cell(-1, -1, Cell.Type.UNKNOWN, this));
                }
            }
        });

        // Calculate doors for each room.
        rooms.values().forEach(Room::calculateDoorSteps);
    }


	/**
	 * Loads all Cell images into memory.
	 * - Loads images
	 * - NOTE: Does NOT Scale!
	 */
	private void setupImageIcons() {
        baseImageIcons.clear();
        scaledImageIcons.clear();
        // Setup ImageIcons
        for (Sprite.SpriteAlias alias : Sprite.SpriteAlias.values()) {
            baseImageIcons.put(Sprite.parseCell(alias), null);
            baseImageIcons.put(Sprite.parseMarker(alias), null);
        }
        for (Cell.Type type : Cell.Type.values()) {
            baseImageIcons.put(Cell.parseImageIcon(type), null);
            baseImageIcons.put(Cell.parseHighLightedImageIcon(type), null);
        }
        for (Cell.Direction dir : Cell.Direction.values()) {
            baseImageIcons.put(Cell.parseWallIcon(dir), null);
        }
        for (Weapon.WeaponAlias alias : Weapon.WeaponAlias.values()) {
            baseImageIcons.put(Weapon.parseWeaponIcon(alias), null);
        }

        baseImageIcons.put("cell_invalid.png", null);
        baseImageIcons.put("cell_visited.png", null);
        baseImageIcons.put("marker_unknown.png", null);

        for (String key : baseImageIcons.keySet()) {
            String fname = key;
            ImageIcon testIcon = new ImageIcon(fname);
            if (testIcon.getIconWidth() <= 0 || testIcon.getIconHeight() <= 0) {
                if (key.startsWith("cell_")) fname = "cell_unknown.png";
                if (key.startsWith("wall_")) fname = "wall_unknown.png";
                if (key.startsWith("marker_")) fname = "marker_unknown.png";
             }
            baseImageIcons.put(key, new ImageIcon(fname));
            scaledImageIcons.put(key, new ImageIcon(fname));
        }
    }


	/**
	 * Scales all images in memory to new size.
	 * - NOTE: Does not load new files
	 * @param cellSize new size of cells.
	 */
	public void scaleIcons(int cellSize) {
		// No need to rescale.
        if (cellSize == prevCellSize) return;

        for (String fname : baseImageIcons.keySet()) {
            Image baseImage = baseImageIcons.get(fname).getImage();
            scaledImageIcons.put(fname, new ImageIcon(baseImage.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH)));
        }
        prevCellSize = cellSize;
    }


	/**
	 * Determines whether Cells should be linked.
	 * True if: One is weapon, either room.
	 * Or Both same type.
	 * False otherwise.
	 * @param a First cell.
	 * @param b Second cell.
	 * @return True if (one is weapon, either room) or both same type.
	 */
	private boolean linkCells(Cell a, Cell b) {
        if ((a.isType(Cell.Type.WEAPON) && b.isType(Cell.Type.ROOM)) || (b.isType(Cell.Type.WEAPON) && a.isType(Cell.Type.ROOM)))
            return true;
        else return a.isType(b.getType());
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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }


    /**
     * moveUser: Move a Game.User to another Game.Cell.
     *
     * @param user   Game.User to move.
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
     *
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
     *
     * @param cord String of coordinate. Ie, H13
     * @return Game.Cell at cord.
     */
    public Cell getCell(String cord) {

        int col, row;
        try {
            col = Character.toUpperCase(cord.charAt(0)) - 'A';
            row = Integer.parseUnsignedInt(cord.substring(1)) - 1;
        } catch (Exception e) {
            throw new InvalidParameterException();
        }

        return getCell(row, col);
    }

	/**
	 * Gets stream of all Cells, left to right, top to bottom.
	 * @return Stream of all cells, left to right, top to bottom.
	 */
	public Stream<Cell> getStream() {
        List<Cell> cellList = new ArrayList<>();
        for (int r = 0; r != rows; ++r) {
            for (int c = 0; c != cols; ++c) {
                cellList.add(cells[r][c]);
            }
        }
        return cellList.stream();
    }
}