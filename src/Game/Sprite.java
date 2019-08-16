package Game;

public class Sprite extends Card {

	/**
	 * An Enum defining the different Sprites in the Game.
	 */
	public enum SpriteAlias {
		MRS_WHITE, MR_GREEN, MRS_PEACOCK, PROFESSOR_PLUM, MISS_SCARLETT, COLONEL_MUSTARD;
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	private User user;
	private SpriteAlias spriteAlias;
	private Cell positionCell;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Game.Sprite: The Constructor for Game.Sprite.
	 * @param spriteAlias SpriteAlias for Game.Sprite to take up.
	 */
	public Sprite(SpriteAlias spriteAlias) {
		super(spriteAlias.toString());
		this.spriteAlias = spriteAlias;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getUser: Return the Game.User playing this Game.Sprite, if any.
	 * @return Game.User playing Game.Sprite.
	 */
	public User getUser() { return user; }


	/**
	 * setUser: Sets a Game.User to play this Game.Sprite.
	 * NOTE: This also Sets the Users Game.Sprite to this!
	 * @param user Game.User to play this Game.Sprite.
	 */
	public void setUser(User user) {
		this.user = user;
		if (user.getSprite() == this) return;
		user.setSprite(this);
	}

	/**
	 * getSpriteAlias: Gets the SpriteAlias of the Game.Sprite.
	 * @return SpriteAlias of the Game.Sprite.
	 */
	public SpriteAlias getSpriteAlias() {
		return spriteAlias;
	}

	/**
	 * setPosition: Set a new position for the Game.Sprite.
	 * Note: This also sets the new Cells Game.Sprite to this,
	 * and old to null.
	 *
	 * @param position Game.Cell of new position.
	 */
	public void setPosition(Cell position) {
		position.setSprite(null);

		this.positionCell = position;
		if (positionCell.getSprite() == this) return;
		position.setSprite(this);

	}

	/**
	 * getPosition: Gets the Game.Cell the Game.Sprite is positioned on.
	 * @return Game.Cell of position.
	 */
	public Cell getPosition() {
		return positionCell;
	}

	/**
	 * parseAliasFromOrdinalChar: Given a digit char, find the matching
	 * SpriteAlias with the matching ordinal position in the Enum.
	 * @param c The digit char corresponding to a SpriteAlias' enum position.
	 * @return The SpriteAlias declared at that enum ordinal.
	 */
	public static SpriteAlias parseAliasFromOrdinalChar(char c) {
		int i = Integer.parseInt(c + "");
		for (SpriteAlias alias : SpriteAlias.values()) {
			if (alias.ordinal() == i)
				return alias;
		}
		throw new IllegalStateException("Error parsing " + c + " as an ordinal for SpriteAlias.");
	}

	/**
	 * parseAliasFromOrdinalChar: Given an, find the matching
	 * SpriteAlias with the matching ordinal position in the Enum.
	 * @param i int corresponding to a SpriteAlias' enum position.
	 * @return SpriteAlias declared at that enum ordinal.
	 */
	public static SpriteAlias parseAliasFromOrdinalInt(int i) {
		int sizeOfCharacterValues = SpriteAlias.values().length;
		if (i >= 0 && i < sizeOfCharacterValues)
			return SpriteAlias.values()[i];
		throw new IllegalStateException("Error parsing " + i + " as an ordinal for SpriteAlias.");
	}

	public String toString() {
		switch(spriteAlias) {
			case MRS_WHITE:
				return "W";
			case MR_GREEN:
				return "G";
			case MRS_PEACOCK:
				return "Q";
			case PROFESSOR_PLUM:
				return "P";
			case MISS_SCARLETT:
				return "S";
			case COLONEL_MUSTARD:
				return "M";
		}
		throw new RuntimeException("Game.Sprite alias not found.");
	}

}