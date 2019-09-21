package game;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds path for the board.
 */
public class PathFinder {

    // Needed for checkValidFromString
    private Board board;
    private Set<Cell> visitedCells;
    private Queue<Cell> path;
    private Set<Room> visitedRooms;

    /**
     * Game.PathFinder: The constructor for Path Finder
     * @param board board
     */
    public PathFinder(Board board) {
        this.board = board;
        this.path = new ArrayDeque<Cell>() {
        };
        this.visitedCells = board.highlightedCells;
        this.visitedRooms = board.highlightedRooms;
    }

    public Queue<Cell> getPath() {
        return path;
    }

    /**
     * findShortestPathFromString: Calculates whether a path from start to end is valid
     * given the provided number of steps.
     * Input Strings: "H3" or "G13"
     *
     * @param start the starting Game.Cell on the map.
     * @param end the desired cell on the map.
     * @return true valid, false otherwise.
     */
    public int findShortestPathFromString(String start, String end) {
        if (board == null) throw new RuntimeException("Game.PathFinder does not have a Game.Board!");
        return findShortestPath(board.getCell(start), board.getCell(end));
    }

    /**
     * findShortestPath: Calculates the shortest number of steps to a given cell.
     *
     * @param start the starting Game.Cell on the map.
     * @param end the desired cell on the map.
     * @return int number of steps using shortest path,
     *  or Integer.MAX_VALUE if none can be found.
     */
    public int findShortestPath(Cell start, Cell end) {
        // NOTE: Commenting will be nearly identical to my 261 asg.  - (Zac Scott 300447976)

        if (end.getSprite() != null || start.sameRoom(end)) return Integer.MAX_VALUE;

        PriorityQueue<AStarNode> priorityQueue = new PriorityQueue<>();
        HashMap<Cell, AStarNode> previousNodes = new HashMap<>();

        // Setup the first Node.
        AStarNode node = new AStarNode(null, start, 0, getDistance(start, end));
        priorityQueue.add(node);
        previousNodes.put(node.current, node);

        while (!priorityQueue.isEmpty()) { // Keeps searching until map is exhausted.
            // Grab the Node.
            node = priorityQueue.poll();
            Cell cell = node.current;

            if (node.current.sameCell(end)) break; // Breaks when path is found.

            // If in a room, iterate over its doors, else its neighbours.
            for (Cell neigh : ((cell.getRoom() != null) ? cell.getRoom().getDoorSteps() : cell.getNeighbors().values())) {
                double distanceTravelled = node.distanceTravelled + 1;
                double heuristic = distanceTravelled + getDistance(neigh, end); // Euclidean distance.

                AStarNode newStarNode = new AStarNode(cell, neigh, distanceTravelled, heuristic);

                if (board.visitedRooms.contains(neigh.getRoom()) || visitedRooms.contains(neigh.getRoom())) continue; // Game.Room forbidden.
                if (board.visitedCells.contains(neigh) || visitedCells.contains(neigh)) continue; // Game.Cell forbidden.
                if (neigh.getSprite() != null) continue; // Character on cell.

                if (previousNodes.containsKey(neigh)) { // Node is already visited.
                    AStarNode oldStarNode = previousNodes.get(neigh);

                    if  (newStarNode.heuristic < oldStarNode.heuristic) { // If shortcut found.
                        priorityQueue.remove(oldStarNode);
                        priorityQueue.add(newStarNode);
                        previousNodes.put(neigh, newStarNode);
                    }
                } else { // Node hasn't been visited yet.
                    priorityQueue.add(newStarNode);
                    previousNodes.put(newStarNode.current, newStarNode);
                }
            }
        }

        if (!node.current.sameCell(end)) return Integer.MAX_VALUE; // Path was unable to be found. End was unreachable.

        Stack<Cell> backwardsPath = new Stack<>(); // Stack of path. NOTE: I could just remember n steps. This was helpful for debugging too!

        while (node != null) { // Work our way back through the nodes.
            backwardsPath.add(node.current);
            node = previousNodes.get(node.previous);
        }

        backwardsPath.pop();

        // Apparently .addAll() will add stack elements in reverse?
        while (!backwardsPath.isEmpty())
            path.add(backwardsPath.pop());

        // Visit all rooms in the path.
        board.highlightedRooms.addAll(path.stream().map(Cell::getRoom).filter(Objects::nonNull).collect(Collectors.toSet()));
        board.highlightedCells.addAll(path);

        // Path Can Be Used here if needed.
        return this.path.size();
    }

    /**
     * findShortestPath: Calculates whether there is a valid path from start to
     * end using exactly 'steps' moves.
     * NOTE:
     *  - Each room counts as only 1 step.
     *  - You cannot visit a cell more than once in a turn.
     *  - A turn is valid even with steps remaining if there is a path to the
     *  target room (if there is one) that is only shorter that 'steps'
     * @param start the starting Game.Cell on the map.
     * @param end the desired cell on the map.
     * @param steps the number of steps that have to be taken.
     * @return true path was found meeting parameters, false otherwise.
     */
    public boolean findExactPathFromString(String start, String end, int steps) {
        return findExactPath(board.getCell(start), board.getCell(end), steps);
    }

    /**
     * findShortestPath: Calculates whether there is a valid path from start to
     * end using exactly 'steps' moves.
     * NOTE:
     *  - Each room counts as only 1 step.
     *  - You cannot visit a cell more than once in a turn.
     *  - A turn is valid even with steps remaining if there is a path to the
     *  target room (if there is one) that is only shorter that 'steps'
     *
     * @param start the starting Game.Cell on the map.
     * @param end the desired cell on the map.
     * @param steps the number of steps that have to be taken.
     * @return true path was found meeting parameters, false otherwise.
     */
    boolean findExactPath(Cell start, Cell end, int steps) {
        if (board == null) throw new RuntimeException("Game.PathFinder does not have a Game.Board!");
        if (!end.isFree() || start.sameRoom(end)) return false;
        return findExactPathHelper(new DFSNode(start, null), end, visitedRooms, visitedCells, new ArrayDeque<Cell>(), steps);
    }

    /**
     * Helper method for path finding.
     *
     * @param node current DFS node
     * @param end target Game.Cell
     * @param parentVisitedRooms the rooms forbidden as they are visited this turn.
     * @return true path was found meeting parameters, false otherwise.
     */
    private boolean findExactPathHelper(DFSNode node, Cell end, Set<Room> parentVisitedRooms, Set<Cell> parentVisitedCells, Queue<Cell> parentPath, int steps) {
        Set<Room> visitedRooms = new HashSet<>(parentVisitedRooms);
        Set<Cell> visitedCells = new HashSet<>(parentVisitedCells);
        Queue<Cell> currentPath = new ArrayDeque<Cell>(parentPath);

        Cell current = node.current;
        visitedCells.add(current);
        currentPath.add(current);

        // Success Termination.
        if (current.sameRoom(end) || (current.sameCell(end) && node.depth == steps)) {
            if (current.getRoom() != null) visitedRooms.add(current.getRoom());
            // Remember path.
            visitedCells.add(current);
            board.highlightedCells = visitedCells;
            board.highlightedRooms = visitedRooms;
            path.clear();
            currentPath.poll();
            path.addAll(currentPath);
            // Return success to parent.
            return true;
        }

        // Failed Termination.
        if (steps < node.depth) return false;

        Set<Cell> neighbours;

        // Select the neighbours, or door ways if in a room.
        if (current.getRoom() != null) {
            neighbours = new HashSet<>(current.getRoom().getDoorSteps());
            visitedRooms.add(current.getRoom());
        } else neighbours = new HashSet<>(current.getNeighbors().values());

        for (Cell neigh : neighbours) {
            if (node.visited.contains(neigh) || visitedRooms.contains(neigh.getRoom()) || visitedCells.contains(neigh)) continue;
            if (board.visitedCells.contains(neigh) || board.visitedRooms.contains(neigh.getRoom())) continue;
            if (!neigh.isFree()) continue; // Game.Sprite on Game.Cell.

            // Return success of child to parent.
            if (findExactPathHelper(new DFSNode(neigh, node), end, visitedRooms, visitedCells, currentPath, steps)) return true;
        }

        // Note we don't add to add all neighbours as visited as there is not path from this Game.Cell.
        return false;
    }

    /**
     * getDistance: Return the Euclidean distance of a Game.Cell from another Game.Cell.
     * (Based on Pythagorean Theorem using row and cols).
     * @param a Game.Cell to measure distance from.
     * @param b Game.Cell to measure distance to.
     * @return Distance between Cells.
     */
    private double getDistance(Cell a, Cell b) {
        return (Math.sqrt(Math.pow(a.getRow() - b.getRow(), 2) + Math.pow(a.getCol() - b.getCol(), 2)));
    }

    private static class DFSNode {
        Cell current;
        Set<Cell> visited = new HashSet<>();
        DFSNode parent; // Useful for debugging.
        int depth = 0;

        private DFSNode(Cell current, DFSNode parent) {
            this.current = current;
            this.parent = parent;

            if (parent == null) return;

            this.visited.add(parent.current);
            this.visited.addAll(parent.visited);
            this.depth = parent.depth + 1;
        }
    }

    /**
     * Private Class to assist with AStarSearch.
     */
    private static class AStarNode implements Comparable<AStarNode> {
        Cell previous, current;
        double distanceTravelled, heuristic;

        private AStarNode(Cell previous, Cell current, double distanceTravelled, double heuristic) {
            this.previous = previous;
            this.current = current;
            this.distanceTravelled = distanceTravelled;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(AStarNode o) { return this.heuristic <= o.heuristic ? -1 : 1; }
    }
}
