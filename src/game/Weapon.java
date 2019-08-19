package game;

public class Weapon extends Card {

	/**
	 * An Enum defining the different Weapons in the Game.
	 */
	public enum WeaponAlias {
		CANDLE_STICK,
		DAGGER,
		LEAD_PIPE,
		REVOLVER,
		ROPE,
		SPANNER
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Game.Weapon Attributes
	private Room room;
	private WeaponAlias weaponAlias;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Game.Weapon: The Constructor for a new Game.Weapon.
	 * @param weaponAlias The WeaponAlias of the Game.Weapon to create.
	 */
	public Weapon(WeaponAlias weaponAlias) {
		super(weaponAlias.toString());
		this.weaponAlias = weaponAlias;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getRoom: Returns the Game.Room containing this Game.Weapon.
	 * @return Return parent Game.Room.
	 */
	public Room getRoom() { return room; }

	/**
	 * setRoom: Replaces the current Game.Room with a new one.
	 * @param room The new Game.Room this Game.Weapon is stored in.
	 */
	public void setRoom(Room room) {
		this.room = room;
		if (room == null || room.getWeapon() == this) return;
		room.setWeapon(this);
	}

	/**
	 * getWeaponAlias: Gets the WeaponAlias of the Game.Weapon.
	 * @return WeaponAlias of the Game.Weapon.
	 */
	public WeaponAlias getWeaponAlias() {
		return weaponAlias;
	}

	/**
	 * Get filename of corresponding Weapon icon.
	 * @param alias Alias of weapon.
	 * @return String filename of alias' corresponding weapon icon.
	 */
	static String parseWeaponIcon(WeaponAlias alias) {
		return "cell_" + alias.toString().toLowerCase() + ".png";
	}

	/**
	 * parseAliasFromOrdinalInt: Given an int, find the matching
	 * WeaponAlias according to its ordinal position in the Enum.
	 * @param i int corresponding to a WeaponAlias' enum position.
	 * @return WeaponAlias declared at that enum ordinal.
	 */
	public static WeaponAlias parseAliasFromOrdinalInt(int i) {
		int sizeOfCharacterValues = WeaponAlias.values().length;
		if (i >= 0 && i < sizeOfCharacterValues)
			return WeaponAlias.values()[i];
		throw new IllegalStateException("Error parsing " + i + " as an ordinal for WeaponAlias.");
	}

	/**
	 * @return String name of Weapon on Card.
	 */
	public String toString() {
		return weaponAlias.toString();
	}
}