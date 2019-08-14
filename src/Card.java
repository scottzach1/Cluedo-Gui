

public abstract class Card {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Card Attributes
	private String name;

	// ------------------------
	// CONSTRUCTOR
	// 

	/**
	 * Card: Constructor for Card.
	 * Stores name of Card, this
	 * should be an Enum toString of a subclass.
	 * @param name Name on Card.
	 */
	public Card(String name) { this.name = name; }

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getName: Get name on Card.
	 * @return Return name on Card.
	 */
	public String getName() {return name;};

	@Override
	public String toString() {
		char icon = name.charAt(0);
		String str = icon + "";
		return str;
	}
}