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

	// Weapon Attributes
	private Room room;
	private WeaponAlias weaponAlias;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Weapon: The Constructor for a new Weapon.
	 * @param weaponAlias The WeaponAlias of the Weapon to create.
	 */
	public Weapon(WeaponAlias weaponAlias) {
		super(weaponAlias.toString());
		this.weaponAlias = weaponAlias;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getRoom: Returns the Room containing this Weapon.
	 * @return Return parent Room.
	 */
	public Room getRoom() { return room; }

	/**
	 * setRoom: Replaces the current Room with a new one.
	 * @param room The new Room this Weapon is stored in.
	 */
	public void setRoom(Room room) {
		this.room = room;
		if (room == null || room.getWeapon() == this) return;
		room.setWeapon(this);
	}

	/**
	 * getWeaponAlias: Gets the WeaponAlias of the Weapon.
	 * @return WeaponAlias of the Weapon.
	 */
	public WeaponAlias getWeaponAlias() {
		return weaponAlias;
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

	public String toString() {
		return weaponAlias.toString();
	}
}