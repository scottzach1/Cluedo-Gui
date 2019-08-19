package game;

import java.awt.*;

public class Sprite extends Card {

	/**
	 * An Enum defining the different Sprites in the Game.
	 */
	public enum SpriteAlias {
		MRS_WHITE, MR_GREEN, MRS_PEACOCK, PROFESSOR_PLUM, MISS_SCARLETT, COLONEL_MUSTARD
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	private User user;
	private SpriteAlias spriteAlias;
	private Cell positionCell;
	private Color spriteColor;
	private Color opposingColor;

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
		setColors(spriteAlias);
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
	 * Checks whether Sprite matches alias.
	 * @param alias Alias to compare.
	 * @return True if matches.
	 */
	public boolean matchesType(SpriteAlias alias) {
		return spriteAlias == alias;
	}

	/**
	 * Get filename of corresponding Cell marker.
	 * @return String filename of corresponding Cell marker.
	 */
	public String getMarker() { return parseMarker(spriteAlias); }

	/**
	 * Get filename of corresponding active Cell.
	 * @return String filename of corresponding active Cell.
	 */
	public String getCell() { return  parseCell(spriteAlias); }

	/**
	 * Get filename of corresponding Cell marker.
	 * @param spriteAlias Alias of marker.
	 * @return String filename of spriteAlias' corresponding Cell marker.
	 */
	static String parseMarker(SpriteAlias spriteAlias) {
		return "marker_" + spriteAlias.toString().toLowerCase() + ".png";
	}

	/**
	 * Get filename of corresponding active Cell.
	 * @param spriteAlias Alias of active Cell.
	 * @return String filename of spriteAlias' corresponding active Cell.
	 */
	static String parseCell(SpriteAlias spriteAlias) {
		return "cell_" + spriteAlias.toString().toLowerCase() + ".png";
	}

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


	/**
	 * @return Colour of Sprite on Board.
	 */
	public Color getSpriteColor(){
		return spriteColor;
	}

	/**
	 * @return Oposing colour of Sprite on Board.
	 */
	public Color getOpposingColor(){
		return opposingColor;
	}

	/**
	 * Sets Colours of Sprite Stored in Board.
	 * @param s Sprite alias to set.
	 */
	private void setColors(SpriteAlias s){
		switch (s) {
		case MRS_WHITE:			spriteColor = new Color(184, 184, 184);
								opposingColor = Color.BLACK;
								break;
		case COLONEL_MUSTARD:	spriteColor = new Color(190, 157, 0);
								opposingColor = Color.BLACK;
								break;
		case MR_GREEN:			spriteColor = new Color(67, 133, 1);
								opposingColor = Color.WHITE;
								break;
		case MRS_PEACOCK:		spriteColor = new Color(42, 85, 120);
								opposingColor = Color.WHITE;
								break;
		case MISS_SCARLETT: 	spriteColor = new Color(139, 5, 4);
								opposingColor = Color.WHITE;
								break;
		case PROFESSOR_PLUM:	spriteColor = new Color(125, 15, 205);
								opposingColor = Color.WHITE;
								break;
		default:                spriteColor = new Color(64, 255, 0, 255);
		                        opposingColor = new Color(255, 90, 0);
	}

	}

	/**
	 * @return String name of Sprite on Card.
	 */
	public String toString() {
		return spriteAlias.toString();
	}
}