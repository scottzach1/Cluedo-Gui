package game;

import gui.GUI;

import java.util.*;

/* Created by Harrison Cook and Zac Scott - 2019 */

/**
 * Game: Controls the steps of the game, creation of objects, and run time
 * actions.
 */
public class CluedoGame {

	public static enum State {
		MAIN_MENU, PLAYER_COUNT, USER_CREATION, SETUP_GAME_DESIGN, RUN_GUI;
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
		// Construct components
		board = new Board(this);
		gui = new GUI(this);
		gui.addLayoutComponents();

		// Set up start menu options
		availableSprites = new HashSet<>(board.getSprites().keySet());
		users = new ArrayList<>();
		losers = new ArrayList<>();
		movesThisTurn = 0;
		movesLeft = 0;
		currentUserNo = User.UserNo.PLAYER_0;
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
			else if (state == State.USER_CREATION)
				gui.createUser(tempUserNum);
			else if (state == State.SETUP_GAME_DESIGN){
				gui.gameSetup();
				generateSolution();
				generateHands();
				nextState();
			}
			else if (state == State.RUN_GUI){
				board.visitedCells.clear();
				board.visitedRooms.clear();
				gui.newPlayer();
			}
	}

	public void checkSuggestion(Card sprite, Card weapon, Card room){
		// Create an empty list of cards
		otherPlayersHand = new ArrayList<>();

		Sprite s = (Sprite) sprite;
		Weapon w = (Weapon) weapon;
		Room r = (Room) room;

		// For every player, go through all their cards and see if they hold any the are being searched for
		for (int i = ((currentUserNo.ordinal() + 1) % playerAmount); i != currentUserNo.ordinal(); i = (i + 1) % playerAmount){
			if(users.get(i).getHand().contains(s))
				otherPlayersHand.add(s);
			if(users.get(i).getHand().contains(w))
				otherPlayersHand.add(w);
			if(users.get(i).getHand().contains(r))
				otherPlayersHand.add(r);

			// If the player had 1 or more cards to refute the suggestion
			if (otherPlayersHand.size() > 0){
				otherPlayer = users.get(i);
				gui.confirmShowHiddenContent();
				return;
			}
		}
	}

	public void chooserHiddenCard(){
		gui.chooseHiddenPlayerCard();
	}

	public void checkAccusation(Card sprite, Card weapon, Card room){
		User user = getCurrentUser();
		if (solution[0] == sprite && weapon == solution[1] && room == solution[2]){
			gui.displayWinner(user);
		}
		else {
			losers.add(user);
			gui.displayLoser(user);
		}
	}

	public void confirmRestartGame(){
		if (gui.restartGame()){
			restartGame();
		}
		else {
			System.exit(0);
		}
	}

	public void restartGame(){
	    gui.dispose();
        CluedoGame g = new CluedoGame();
        g.gameController();

	}

	/**
	 * tryMove: Uses the Game.PathFinder test is a suggested path is feasible
	 *
	 * @param end      - Target Game.Cell
	 * @param moveType - Move type a player is making
	 * @param user     - Who is attempting the move
	 * @return String - "8" allowing for the next player to take their turn if
	 *         successful
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

	public Board getBoard(){
		return board;
	}

	public GUI getGui(){
		return gui;
	}

	public void addNewUser(){
		User u = new User();
		u.setUserName(tempUserName);
		u.setSprite(board.getSprites().get(tempSpriteChoice));
		board.getSprites().get(tempSpriteChoice).setUser(u);
		users.add(u);
	}

	public void nextState() {
		if (state != State.RUN_GUI)
			state = State.values()[state.ordinal() + 1];
		else {
			currentUserNo = User.UserNo.values()[(currentUserNo.ordinal() + 1) % playerAmount];
			if (losers.contains(users.get(currentUserNo.ordinal()))){
				gui.skipUser(losers.get(currentUserNo.ordinal()));
			}
		}
		gameController();
	}

	public State getState(){
		return state;
	}

	public int rollDie() {
		Random dice = new Random();
		return dice.nextInt(6) + 1;
	}

	public void setMovesThisTurn(int moves){
		movesThisTurn = moves;
		movesLeft = moves;
	}

	public int getMovesLeft(){
		return movesLeft;
	}

	public void removeMovesLeft(int usedMoves){
		movesLeft -= usedMoves;
	}

	public boolean hasMovesLeft(){
		return movesLeft > 0;
	}

	public User getCurrentUser(){
		return users.get(currentUserNo.ordinal());
	}

	public void setShowOtherPlayerCard(Card c){
        otherPlayerCard = c;
	}

	public User getOtherPlayer(){
		return otherPlayer;
	}

	public Card getOtherPlayerCard(){
		return otherPlayerCard;
	}

	public ArrayList<Card> getOtherPlayerHand(){
	    return otherPlayersHand;
    }


	// Setup getters, and setters
	public void setPlayerAmount(int playerAmount) {
		this.playerAmount = playerAmount;
	}

	public Set<Sprite.SpriteAlias> getAvailableSprites(){
		return availableSprites;
	}

	public void removeAvailableSprite(Sprite.SpriteAlias s){
		availableSprites.remove(s);
	}

	public void setTempUserName(String un) {
		this.tempUserName = un;
	}

	public void setTempSprite(Sprite.SpriteAlias sa) {
		tempSpriteChoice = sa;
	}

	public void nextTempUserNum() {
		addNewUser();
		tempUserNum++;
		if (tempUserNum < playerAmount) {
			gameController();
		}
		else {
			nextState();
		}
	}


	public static void main(String[] args) {
		// Setup
		CluedoGame g = new CluedoGame();
	}

}