package game;

import gui.GUI;

import java.util.*;

/* Created by Harrison Cook and Zac Scott - 2019 */

/**
 * Game: Controls the steps of the game, creation of objects, and run time
 * actions.
 */
public class CluedoGame {

	public static enum STATE{
		MAIN_MENU, PLAYER_COUNT, USER_CREATION, START_GAME;
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Setup attributes
	private int playerAmount;
	private String tempUserName;
	private Sprite.SpriteAlias tempSpriteChoice;
	private Set<Sprite.SpriteAlias> availableSprites;
	private int tempUserNum;


	// Game Attributes
	private Board board;
	private PathFinder pathFinder;
	private List<User> users;
	private List<User> losers;
	private Card[] solution;
	private String status;
	private GUI gui;
	private STATE state;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Game.CluedoGame: Constructor
	 */
	public CluedoGame() {
		// Starting state
		state = STATE.MAIN_MENU;
		// Construct components
		board = new Board();
		gui = new GUI(this);
		gui.addLayoutComponents();

		// Set up start menu options
		availableSprites = new HashSet<>(board.getSprites().keySet());
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * gameController: Maintains the order of the game
	 */
	private void gameController() {
		System.out.println(state);
			if (state == STATE.MAIN_MENU)
				gui.mainMenu();
			else if (state == STATE.PLAYER_COUNT)
				gui.howManyPlayers();
			else if (state == STATE.USER_CREATION)
				gui.createUser(tempUserNum);
			else if (state == STATE.START_GAME){
				gui.runGame();
			}
	}

	/**
	 * mainMenu: Orders and runs the LUI methods for the main menu to print, and
	 * acts upon user input
	 */
	private void mainMenu() {
	}

	/**
	 * gameSetup: Orders and runs the LUI methods for the game set up stage to
	 * print, and acts upon user input
	 */
	private void gameSetup() {
	}

	/**
	 * rounds: Acts like a forever loop, controlling what the LUI prints and acts
	 * upon user inputs
	 */
	/**
	 *
	 */
	private void rounds() {
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
		state = STATE.values()[state.ordinal() + 1];
		gameController();
	}

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
		tempUserNum++;
		if (tempUserNum < playerAmount) {
			System.out.println("YO");
			gameController();
		}
		else {
			System.out.println("YO");
			nextState();
		}
	}


	public static void main(String[] args) {
		// Setup
		CluedoGame g = new CluedoGame();
		g.gameController();
	}

}