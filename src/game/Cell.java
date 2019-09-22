package game;

import extra.CombinedImageIcon;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A cell on the board.
 */
public class Cell extends JLabel implements MouseListener {

    /**
     * An Enum defining the different possible directions of neighbours of a Game.Cell.
     */
    public enum Direction {
        NORTH, EAST, SOUTH, WEST;

        Direction reverse() {
            return values()[ordinal() + ((ordinal() > 1) ? -2 : 2)];
        }
    }

    /**
     * An Enum defining the different kinds of Cells in the Game.
     */
    public enum Type {ROOM, HALL, START_PAD, VOID, CELLAR, UNKNOWN, WEAPON}

    /**
     * Get corresponding cell on Board in direction.
     *
     * @param cells Cell array of board.
     * @param dir   Direction of Cell to go.
     * @return Cell in direction dir.
     */
    public Cell go(Cell[][] cells, Direction dir) {
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
     * @param board The board
     */
    public Cell(int row, int col, Cell.Type type, Board board) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.sprite = null;
        this.board = board;
        neighbors = new HashMap<>();
        icons = Board.scaledImageIcons;
        this.addMouseListener(this);
    }

    /**
     * Render Cell on Swing Frame.
     * - Shows Highlighted overlay if highlighted.
     * - Shows Player marker overlay if sprite on Cell.
     * - Shows Player active cell if active sprite on Cell.
     * - Shows Weapon if Weapon cell and weapon in Room.
     *
     * @return Rendered Cell.
     */
    public Cell render() {
        List<ImageIcon> layers = new ArrayList<>();

        // Get base layer.
        ImageIcon base;
        if (board.highlightedCells.contains(this)) {
            base = icons.get(parseHighLightedImageIcon(type));
        } else base = icons.get(parseImageIcon(type));

        // Add walls to layers.
        for (Direction dir : Direction.values()) {
            if (neighbors.get(dir) == null) layers.add(icons.get(parseWallIcon(dir)));
        }

        Sprite.SpriteAlias currentAlias = board.cluedoGame.getCurrentUser().getSprite().getSpriteAlias();

        // Add sprite if on Cell. (maker if active, whole cell active).
        if (sprite != null) {
            if (sprite.matchesType(currentAlias))
                base = icons.get(sprite.getCell());
            else layers.add(icons.get(sprite.getMarker()));
            setToolTipText(sprite.getSpriteAlias().toString());
        }

        // Add weapon to layers.
        if (hasWeapon()) {
            layers.add(icons.get(Weapon.parseWeaponIcon(getWeapon().getWeaponAlias())));
            setToolTipText(getWeapon().getWeaponAlias().toString());
        } else if (isType(Type.WEAPON)) setToolTipText("EMPTY_SLOT");

        // Add visited overlay if visited.
        if (board.visitedCells.contains(this) && (sprite == null || !sprite.matchesType(currentAlias)))
            layers.add(icons.get("cell_visited.png"));

        // Calculate and set new Icon.
        setIcon(prevIcon = new CombinedImageIcon(base, layers));
        return this;
    }

    /**
     * Get filename of Wall in Direction dir.
     *
     * @param dir Direction wall to obtain.
     * @return String filename.
     */
    static String parseWallIcon(Direction dir) {
        return "wall_" + dir.toString().toLowerCase() + ".png";
    }

    /**
     * Get filename of Cell based on Type.
     *
     * @param type Type of Cell.
     * @return String filename.
     */
    static String parseImageIcon(Cell.Type type) {
        return "cell_" + type.toString().toLowerCase() + ".png";
    }

    /**
     * Get filename of highlighted Cell based on type.
     *
     * @param type Type of Cell.
     * @return String filename.
     */
    static String parseHighLightedImageIcon(Cell.Type type) {
        if (type == Type.WEAPON) return parseImageIcon(type);
        return "cell_" + type.toString().toLowerCase() + "_highlighted.png";
    }

    // Useful to help other Cells to know not to respond to user input until cell is free.
    private static boolean currentlyMoving = false;

    /**
     * Mouse was clicked on the Cell:
     * Apply move to Board if Valid, otherwise prompt user invalid move.
     *
     * @param e Mouse Event.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Reject input if currently moving
        if (currentlyMoving) return;

        // If Invalid Move.
        if (board.pathFinder.getPath().stream().anyMatch(board.visitedCells::contains) || board.highlightedCells.isEmpty()) {
            board.cluedoGame.getGui().infeasibleMove();
            return;
        }

        // Store path.
        Queue<Cell> path = new ArrayDeque<Cell>(board.pathFinder.getPath());

        // Create Timer for animation.
        javax.swing.Timer t = new javax.swing.Timer(50, null);
        t.setRepeats(false); // Stop timer from forever repeating.
        t.addActionListener(e1 -> {
            currentlyMoving = true;

            // Grab Variables
            User user = board.cluedoGame.getCurrentUser();
            Cell from = user.getSprite().getPosition();
            Cell to = path.poll();
            if (to == null) throw new Error("Cell should not be null");

            // Update collections
            board.visitedCells.add(from);
            board.highlightedCells.remove(to);
            if (from.hasRoom()) {
                from.getRoom().getCells().forEach(cell -> {
                    board.highlightedCells.remove(cell);
                    board.visitedCells.add(cell);
                });
            }
            if (to.hasRoom()) {
                to.getRoom().getCells().forEach(cell -> {
                    board.highlightedCells.remove(cell);
                    board.visitedCells.add(cell);
                });
            }

            // Move player
            board.moveUser(user, to);
            board.cluedoGame.removeMovesLeft(1);
            board.cluedoGame.getGui().redrawDice();

            // Render changes
            board.render();

            // Restart timer for next movement.
            if (!path.isEmpty()) t.restart();
            // Clear memory
            else {
                // Remember Cells.
                board.visitedCells.addAll(board.highlightedCells);

                // Clear Highlighted for next Movement.
                board.highlightedCells.clear();
                board.highlightedRooms.clear();
                board.pathFinder.getPath().clear();

                if (to != this)
                    board.moveUser(board.cluedoGame.getCurrentUser(), this);

                // Render changes
                board.render();
                board.cluedoGame.getGui().redrawDice();
            }
            currentlyMoving = false;
        });

        t.start();
    }

    /**
     * Mouse Hover over Cell:
     * Calculate Path to Cell, based on CluedoGames path finding mode.
     * - Calculate Path
     * - Add all Cells in path to highlighted Cells.
     *
     * @param e Mouse Event.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // Reject input if currently moving
        if (currentlyMoving) return;

        // Don't find path if Cell not on board.
        if (isType(Type.VOID)) return;

        // Grab values.
        CluedoGame cluedoGame = board.cluedoGame;
        int movesLeft = cluedoGame.getMovesLeft();
        Cell startingCell = cluedoGame.getCurrentUser().getSprite().getPosition();
        PathFinder pathFinder = board.pathFinder;

        // Reset Highlighted collections.
        pathFinder.getPath().clear();
        board.highlightedCells.clear();
        board.highlightedRooms.clear();

        boolean success; // Remember success of PathFinder here.

        // Use Shortest Path
        if (CluedoGame.shortestPath)
            success = movesLeft >= pathFinder.findShortestPath(startingCell, this);
            // Else Use Exact Path.
        else success = pathFinder.findExactPath(startingCell, this, movesLeft);

        // Success, also Highlight cells in rooms visited by pathfinder.
        if (success) board.highlightedRooms.forEach(room -> board.highlightedCells.addAll(room.getCells()));
        else { // Else, clear highlighted collections.
            board.highlightedCells.clear();
            board.highlightedRooms.clear();
        }
        // Render board.
        board.render();
        // If no path to cell, render 'X' on Cell.
        if (!success && startingCell != this) setIcon(new CombinedImageIcon(prevIcon, icons.get("cell_invalid.png")));
    }

    /**
     * Mouse Left Cell:
     * - Clear all highlighted Cells
     * - Rerender Board.
     * @param e Mouse event.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // Reject input if currently moving
        if (currentlyMoving) return;

        board.highlightedRooms.clear();
        board.highlightedCells.clear();

        board.render();
    }

    /**
     * Implemented but ignored
     * @param e Mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Implemented but ignored
     * @param e Mouse event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    /**
     * If Cell's type is Room, get Rooms weapon.
     * @return weapon
     */
    public Weapon getWeapon() {
        if (!hasRoom() || !isType(Type.WEAPON)) return null;
        return room.getWeapon();
    }

    /**
     * @return True if Cell can be visited by Sprite
     * (True if a hall or room, and no sprite on it.
     */
    public boolean isFree() {
        return (hasRoom() || (isType(Type.HALL) && sprite == null));
    }

    /**
     * @return True if Weapon Cell, in a room with a Weapon.
     */
    public boolean hasWeapon() {
        return getWeapon() != null;
    }

    /**
     * @return True if Cell has a Room.
     */
    public boolean hasRoom() {
        return room != null;
    }

    /**
     * @return True if Cell is missing a room. (And expecting, ie not hall).
     */
    public boolean missingRoom() {
        return (isType(Type.ROOM) || isType(Type.WEAPON)) && room == null;
    }

    /**
     * Checks whether Cell matches a corresponding type.
     *
     * @param type Type to compare.
     * @return True of Cell shared type with param.
     */
    public boolean isType(Type type) {
        return type == getType();
    }

    /**
     * getType: Get the Type of the Game.Cell.
     *
     * @return the Type of the Game.Cell.
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the Game.Sprite on the Game.Cell, if any.
     *
     * @return Get any Game.Sprite on the Game.Cell currently.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * setSprite: Place a sprite on the Game.Cell.
     *
     * @param sprite Game.Sprite to place on Game.Cell.
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * getRoom: Get the Game.Room a cell is in, if any.
     *
     * @return The Game.Room of the Game.Cell.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * setRoom: Sets the Game.Room of this Game.Cell.
     *
     * @param room The Game.Room to set for this Game.Cell.
     */
    public void setRoom(Room room) {
        this.room = room;
        if (room == null || room.getCells().contains(this)) return;
        room.addCell(this);
    }

    /**
     * getCol: Get the Column of the Game.Cell.
     *
     * @return Col of Game.Cell.
     */
    int getCol() {
        return col;
    }

    /**
     * getRow: Get the Row of the Game.Cell.
     *
     * @return Row of Game.Cell.
     */
    int getRow() {
        return row;
    }

    /**
     * getNeighbours: Return a Map of all the neighbours of the Game.Cell, with Direction as the Key.
     *
     * @return Map of Neighbours.
     */
    public HashMap<Direction, Cell> getNeighbors() {
        return neighbors;
    }

    /**
     * setNeighbour: Set a neighbour for a direction.
     *
     * @param dir  Direction to add neighbour.
     * @param cell Game.Cell to add as neighbour.
     */
    public void setNeighbor(Direction dir, Cell cell) {
        neighbors.put(dir, cell);
    }

    /**
     * getType: Get the type of Game.Cell based on the corresponding character on the raw data map. (MapBase.txt)
     *
     * @param c char of Game.Cell.
     * @return Type of Game.Cell.
     */
    public static Type getType(char c) {
        if (c == 'M') return Type.CELLAR;
        if (c == 'W') return Type.WEAPON;
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
     *
     * @return String of Game.Cell coordinates.
     */
    public String getStringCoordinates() {
        return "" + ((char) (col + 'A')) + (row + 1);
    }

    /**
     * sameCell: Checks whether both cells are the same, or share the same non null room.
     *
     * @param other second cell to compare.
     * @return boolean true if same, false otherwise.
     */
    public boolean sameCell(Cell other) {
        return sameRoom(other) || this == other;
    }

    /**
     * sameRoom: Checks whether both Cells share a non null room.
     *
     * @param other second cell to compare.
     * @return true if non null room shared, false other wise.
     */
    public boolean sameRoom(Cell other) {
        return (hasRoom() && this.getRoom() == other.getRoom());
    }
}
