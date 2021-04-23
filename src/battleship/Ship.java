package battleship;

/**
 * Ships that can be placed on a playing field.
 */
public enum Ship {
    CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String name;
    private final int size;

    /**
     * Constructor for each ship.
     * @param name full name of ship
     * @param size the required length of ship
     */
    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    /**
     * Returns the full name of the ship
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the length of the ship
     * @return size
     */
    public int getSize() {
        return size;
    }
}
