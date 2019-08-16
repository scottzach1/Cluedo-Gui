package Game;

import java.util.*;

public class User {

	private static int USERS = 0;

	/**
	 * An Enum defining the player number of a user
	 * in the game.
	 */
	public enum userNo {
		PLAYER_0,
		PLAYER_1,
		PLAYER_2,
		PLAYER_3,
		PLAYER_4,
		PLAYER_5
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	private userNo userNum;
	private String userName;
	private List<Card> hand;
	private Set<Card> observedCards;
	private Sprite sprite;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------


	/**
	 * Game.User: The Constructor for a new Game.User.
	 * Auto increment USERS.
	 */
	public User() {
		User.USERS++; // FIXME: HARRISON DO WE WANT TO INCREMENT FIRST?
		userNum = userNo.values()[User.USERS];
		hand = new ArrayList<>();
		observedCards = new HashSet<>();
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getUserNo: Returns the UserNo of the Game.User.
	 * @return UserNo of user.
	 */
	public userNo getUserNo() {
		return userNum;
	}

	/**
	 * getUserName: Returns the UserName of the Game.User.
	 * @return String userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * setUserName: Set the Users userName
	 * @param userName string to set userName to.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * getHand: Returns a List of the cards in Users hand.
	 * @return List of cards in users hand.
	 */
	public List<Card> getHand() {
		return hand;
	}

	/**
	 * addToHand: Adds a Game.Card to the Users hand.
	 * @param card Game.Card to add to hand.
	 */
	public void addToHand(Card card) {
		this.hand.add(card);
	}

	/**
	 * getObservedCards: Returns a Set of all the Cards observed by the Game.User in the Game.
	 * @return Set of observed Cards.
	 */
	public Set<Card> getObservedCards() {
		return observedCards;
	}

	/**
	 * addToObservedCards: Adds a card to the Set of all the Cards observed by the Game.User in the Game.
	 * @param knownCard Game.Card to add to observed Cards.
	 */
	public void addToObservedCards(Card knownCard) {
		this.observedCards.add(knownCard);
	}

	/**
	 * getSprite: Return the Users Game.Sprite on the Game.Board.
	 * @return Game.Sprite of Game.User on Game.Board.
	 */
	public Sprite getSprite() {
		return sprite;
	}


	/**
	 * setSprite: Sets the Users Game.Sprite on the Game.Board.
	 * NOTE: This also sets the Sprites Game.User to Game.User.
	 * @param sprite Game.Sprite to set for Game.User.
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		if (sprite.getUser() == this) return;
		sprite.setUser(this);
	}

	/**
	 * observedContainsAlias: Checks whether the Game.User
	 * has observed a Game.Card based off its Alias toString.
	 * @param s String of card to check.
	 * @return true seen, false otherwise.
	 */
	public boolean observedContainsAlias(String s) {
		for (Card c : observedCards) {
			if (c.getName().equals(s))
				return true;
		}
		return false;
	}

	public String toString() {
		return userName + "(" + userNum + ")";
	}

	/**
	 * resetUserNoCounter: Resets the UserNo static counter. Useful for testing and new games.
	 */
	public static void resetUserNoCounter() { USERS = 0; }

	/**
	 * getUserNoCounter: Gets the number of Users created since last reset.
	 * @return number of Users created since last reset.
	 */
	public static int getUserNoCounter() { return USERS; }
}
