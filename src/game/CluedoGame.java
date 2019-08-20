package game;

import gui.GUI;

import java.util.*;

/* Created by Harrison Cook and Zac Scott - 2019 */

/**
 * Game: Controls the steps of the game, creation of objects, and run time
 * actions.
 */
public class CluedoGame {

    /**
     * State:
     * - An enum to hold track of the current game status
     */
    public static enum State {
        MAIN_MENU, PLAYER_COUNT, USER_NAME_CREATION, USER_CHARACTER_SELECTION, SETUP_GAME_DESIGN, RUN_GUI;
    }

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // Setup attributes
    public static boolean shortestPath = true;
    private int playerAmount;
    private String tempUserName;
    private Sprite.SpriteAlias tempSpriteChoice;
    private Set<Sprite.SpriteAlias> availableSprites;
    private int tempUserNum;


    // Game Attributes
    private Board board;
    private List<User> users;
    private List<User> losers;
    private Card[] solution;
    private GUI gui;
    private State state;
    private int movesThisTurn;
    private int movesLeft;
    private User.UserNo currentUserNo;
    private Card otherPlayerCard;
    private User otherPlayer;
    private ArrayList<Card> otherPlayersHand;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    /**
     * Game.CluedoGame: Constructor
     */
    public CluedoGame() {
        // Starting state
        state = State.MAIN_MENU;
        User.resetUserNoCounter();
        // Construct components
        board = new Board(this);
        gui = new GUI(this);
        gui.addLayoutComponents();
        gui.setGuiState(GUI.GUIState.MAIN_MENU);

        // Set up start menu options
        availableSprites = new HashSet<>(board.getSprites().keySet());
        users = new ArrayList<>();
        losers = new ArrayList<>();
        movesThisTurn = 0;
        movesLeft = 0;
        currentUserNo = User.UserNo.PLAYER_0;
        gameController();
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    /**
     * gameController: Maintains the order of the game
     */
    public void gameController() {
        if (state == State.MAIN_MENU)
            gui.mainMenu();
        else if (state == State.PLAYER_COUNT)
            gui.howManyPlayers();
        else if (state == State.USER_NAME_CREATION)
            gui.createUser();
        else if (state == State.USER_CHARACTER_SELECTION){
            gui.selectCharacter();
        }
        else if (state == State.SETUP_GAME_DESIGN) {
            gui.gameSetup();
            generateSolution();
            generateHands();
            nextState();
        } else if (state == State.RUN_GUI) {
            board.visitedCells.clear();
            board.visitedRooms.clear();
            gui.runGUI();
        }
    }

    /**
     * Runs through all the cards in other players hands and
     * determines who is able to refute the claim first, if any.
     * @param sprite - the suggested sprite card
     * @param weapon - the suggested weapon card
     * @param room - the suggested room card
     */
    public void checkSuggestion(Sprite sprite, Weapon weapon, Room room) {
        // Create an empty list of cards
        otherPlayersHand = new ArrayList<>();

        // If the user is not in a room (an overlooked error somehow)
        if (room == null) {
            gui.setErrorMsg("You are not in a room!");
            gui.setGuiState(GUI.GUIState.PRINT_ERROR);
            gameController();
        }

        //Get a list of all the cells
        ArrayList<Cell> shuffledCells = new ArrayList<>(room.getCells());

        // Shuffle till not on a person
        while(!shuffledCells.get(0).isFree())
        	Collections.shuffle(shuffledCells);
        Cell holdingCell = sprite.getPosition();
        sprite.setPosition(shuffledCells.get(0));
        holdingCell.setSprite(null);

        // Teleport the suggested Weapon to this room too
        Weapon holdingWeapon = null;
        Room holdingRoom = null;

        // If room A has a weapon
        if (room.getWeapon() != null) {
            // Temp hold the rooms weapon
            holdingWeapon = room.getWeapon();
            // Temp hold Room B (room containing weapon)
            holdingRoom = weapon.getRoom();

        }
        // Room A set weapon from Room B
        room.setWeapon(weapon);
        holdingRoom.setWeapon(null);
        if (holdingWeapon != null) {
            // Room B set Weapon from Room A
            holdingRoom.setWeapon(holdingWeapon);
        }

        // For every player, go through all their cards and see if they hold any the are being searched for
        for (int i = ((currentUserNo.ordinal() + 1) % playerAmount); i != currentUserNo.ordinal(); i = (i + 1) % playerAmount) {
            if (users.get(i).getHand().contains(sprite))
                otherPlayersHand.add(sprite);
            if (users.get(i).getHand().contains(weapon))
                otherPlayersHand.add(weapon);
            if (users.get(i).getHand().contains(room))
                otherPlayersHand.add(room);

            // If the player had 1 or more cards to refute the suggestion
            if (otherPlayersHand.size() > 0) {
                otherPlayer = users.get(i);
                gui.confirmShowHiddenContent();
                return;
            }
        }

        if (otherPlayersHand.size() == 0){
            gui.noSuggestions();
            return;
        }
    }

    /**
     * checks the accused sprite weapon and room against the solution cards
     * if the suggestion is wrong then the current player is out of the game
     * if it is right the current player wins the game
     * @param sprite - the accused sprite card
     * @param weapon - the accused weapon card
     * @param room - the accused room card
     */
    public void checkAccusation(Sprite sprite, Weapon weapon, Room room) {
        // If the player guessed correctly
        if (solution[0] == sprite && weapon == solution[1] && room == solution[2]) {
            gui.displayWinner();
        }
        // If the players guess was incorrect
        else {
            losers.add(getCurrentUser());
            gui.displayLoser();
        }
    }

    /**
     * If the players have confirmed to restart the game, the restart the game
     * else exit the game.
     */
    public void confirmRestartGame() {
        if (gui.restartGame()) {
            restartGame();
        }
    }

    /**
     * Dispose of the GUI and recreate the cludeoGame as this will restart
     * the entire process
     */
    public void restartGame() {
        gui.dispose();
        CluedoGame g = new CluedoGame();
    }

    /**
     * tryMove: Uses the Game.PathFinder test is a suggested path is feasible
     *
     * @param end      - Target Game.Cell
     * @param moveType - Move type a player is making
     * @param user     - Who is attempting the move
     * @return String - "8" allowing for the next player to take their turn if
     * successful
     */
    private String tryMove(Cell end, User user, String moveType) {
        return "";
    }

    /**
     * generateSolution: Randomly selects 3 cards, 1 Player, 1 Game.Weapon, 1 Game.Room, and
     * places in the field array 'solution'
     */
    private void generateSolution() {
        // Create new array (empty array), make random object
        solution = new Card[3];
        Random random = new Random();

        // Randomly get a room, sprite, weapon from the board
        Room room = board.getRooms().get(Room.parseAliasFromOrdinalInt(random.nextInt(9)));
        Sprite sprite = board.getSprites().get(Sprite.parseAliasFromOrdinalInt(random.nextInt(6)));
        Weapon weapon = board.getWeapons().get(Weapon.parseAliasFromOrdinalInt(random.nextInt(6)));

        // Set the three cards
        solution[0] = sprite;
        solution[1] = weapon;
        solution[2] = room;
    }

    /**
     * generateHands: Randomly deal out the cards that are not solution cards
     */
    private void generateHands() {
        // Get the list for each sprite, weapon, room
        Map<Sprite.SpriteAlias, Sprite> nonSolutionSprites = new HashMap<>(board.getSprites());
        Map<Weapon.WeaponAlias, Weapon> nonSolutionWeapons = new HashMap<>(board.getWeapons());
        Map<Room.RoomAlias, Room> nonSolutionRooms = new HashMap<>(board.getRooms());

        // Remove solution cards
        nonSolutionSprites.remove(((Sprite) solution[0]).getSpriteAlias());
        nonSolutionWeapons.remove(((Weapon) solution[1]).getWeaponAlias());
        nonSolutionRooms.remove(((Room) solution[2]).getRoomAlias());

        // Create lists of random order for the three maps
        ArrayList<Sprite> randomSprites = new ArrayList<>(nonSolutionSprites.values());
        Collections.shuffle(randomSprites);
        ArrayList<Weapon> randomWeapons = new ArrayList<>(nonSolutionWeapons.values());
        Collections.shuffle(randomWeapons);
        ArrayList<Room> randomRooms = new ArrayList<>(nonSolutionRooms.values());
        Collections.shuffle(randomRooms);

        // Deal the cards out, change user every deal
        int userNum = 0;

        for (int s = 0; s < randomSprites.size(); s++) {
            // Get the user
            User user = users.get(userNum);
            // Get a random sprite
            Sprite randomSprite = randomSprites.get(s);
            // Add this sprite to the users hand and observed cards
            user.addToHand(randomSprite);
            user.addToObservedCards(randomSprite);
            // Change user
            userNum = (userNum + 1) % users.size();
        }

        for (int w = 0; w < randomWeapons.size(); w++) {
            // Get the user
            User user = users.get(userNum);
            // Get a random weapon
            Weapon randomWeapon = randomWeapons.get(w);
            // Add this weapon to the users hand and observed cards
            user.addToHand(randomWeapon);
            user.addToObservedCards(randomWeapon);
            // Change user
            userNum = (userNum + 1) % users.size();
        }

        for (int r = 0; r < randomRooms.size(); r++) {
            // Get the user
            User user = users.get(userNum);
            // Get a random room
            Room randomRoom = randomRooms.get(r);
            // Add this room to the users hand an observed cards
            user.addToHand(randomRoom);
            user.addToObservedCards(randomRoom);
            // Change the user
            userNum = (userNum + 1) % users.size();
        }

    }

    /**
     * Returns the board in the CluedoGame object
     * @return - this.board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the GUI object from the CluedoGame object
     * @return - this.gui
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * Creates a user and sets the name and sprite based on preset fields
     * then
     * Adds a new user to the list of users
     */
    public void addNewUser() {
        User u = new User();
        u.setUserName(tempUserName);
        u.setSprite(board.getSprites().get(tempSpriteChoice));
        board.getSprites().get(tempSpriteChoice).setUser(u);
        users.add(u);
    }

    /**
     * Goes to the next state of the CluedoGame, or Runs the GUI which will determine its
     * own state
     */
    public void nextState() {
        if (state != State.RUN_GUI) {
            state = State.values()[state.ordinal() + 1];
        } else {
            currentUserNo = User.UserNo.values()[(currentUserNo.ordinal() + 1) % playerAmount];
            if (losers.contains(users.get(currentUserNo.ordinal()))) {
                gui.skipUser(getCurrentUser());
            }
            gui.setGuiState(GUI.GUIState.NEW_PLAYER);
        }
        gameController();
    }

    /**
     * Returns the state of this CluedoGame
     * @return - this.state
     */
    public State getState() {
        return state;
    }

    /**
     * Returns a randomly generated number between 1 and 6.
     * @return - random number between 1 and 6
     */
    public int rollDie() {
        Random dice = new Random();
        return dice.nextInt(6) + 1;
    }

    /**
     * Sets the amount of moves the user can use this turn,
     * and resets the amount of moves the user has left this turn
     * @param moves - amount of moves the player has for their turn
     */
    public void setMovesThisTurn(int moves) {
        movesThisTurn = moves;
        movesLeft = moves;
    }

    /**
     * Returns the amount of moves the player has left in their turn
     * @return - this.movesLeft
     */
    public int getMovesLeft() {
        return movesLeft;
    }

    /**
     * Removes move that the player has left
     * @param usedMoves - how many moves to remove
     */
    public void removeMovesLeft(int usedMoves) {
        movesLeft -= usedMoves;
    }

    /**
     * Get the temporary user, used during the setup of the game to
     * ensure that the GUI displays the correct player number
     * @return this.tempUserNum
     */
    public int getTempUserNum(){
        return tempUserNum;
    }

    /**
     * Get the temporary user name, used during the setup of the game
     * to ensure that the GUI displays the correct player name
     * @return this.tempUserName
     */
    public String getTempUserName(){
        return tempUserName;
    }

    /**
     * Returns the player who's turn it is
     * @return the current user
     */
    public User getCurrentUser() {
        return users.get(currentUserNo.ordinal());
    }

    /**
     * Sets the card to show another player when a suggestion has been refuted
     * @param c - The card to show
     */
    public void setShowOtherPlayerCard(Card c) {
        otherPlayerCard = c;
    }

    /**
     * Gets the player making a refute
     * @return - USER refuting player
     */
    public User getOtherPlayer() {
        return otherPlayer;
    }

    /**
     * Gets the card that the other player is using to refute a suggestion
     * @return - this.otherPlayerCard
     */
    public Card getOtherPlayerCard() {
        return otherPlayerCard;
    }

    /**
     * Gets the refuting players hand so that they can choose which card to refute with
     * @return - this.otherPlayerHand
     */
    public ArrayList<Card> getOtherPlayerHand() {
        return otherPlayersHand;
    }

    /**
     * Sets the amount of players in this game
     * @param playerAmount
     */
    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }

    /**
     * Returns a list of all the available sprites
     * @return - Set<Sprite.SpriteAlias>
     */
    public Set<Sprite.SpriteAlias> getAvailableSprites() {
        return availableSprites;
    }

    /**
     * Removes a sprite from being available, used
     * after another character has selected their character
     * @param s - Sprite alias to be removed
     */
    public void removeAvailableSprite(Sprite.SpriteAlias s) {
        availableSprites.remove(s);
    }

    /**
     * Sets the temporary user name, used when inbetween states of
     * creating the character
     * @param un - The users name
     */
    public void setTempUserName(String un) {
        this.tempUserName = un;
    }

    /**
     * Sets the temporary sprite alias that the temporary user is
     * going to be, used in between states when creating a character
     * @param sa - The sprite alias
     */
    public void setTempSprite(Sprite.SpriteAlias sa) {
        tempSpriteChoice = sa;
    }

    /**
     * Gets the next user to create
     * if there are no more users to create it runs onto the next state
     */
    public void nextTempUserNum() {
        addNewUser();
        tempUserNum++;
        if (tempUserNum < playerAmount) {
            state = State.USER_NAME_CREATION;
            gameController();
        } else {
            nextState();
        }
    }


    public static void main(String[] args) {
        // Setup
        CluedoGame g = new CluedoGame();
    }

}