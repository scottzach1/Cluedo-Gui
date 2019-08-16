package game;

public abstract class Card {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Game.Card Attributes
	private String name;

	// ------------------------
	// CONSTRUCTOR
	// 

	/**
	 * Game.Card: Constructor for Game.Card.
	 * Stores name of Game.Card, this
	 * should be an Enum toString of a subclass.
	 * @param name Name on Game.Card.
	 */
	public Card(String name) { this.name = name; }

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getName: Get name on Game.Card.
	 * @return Return name on Game.Card.
	 */
	public String getName() {return name;};

	@Override
	public String toString() {
		char icon = name.charAt(0);
		String str = icon + "";
		return str;
	}
}