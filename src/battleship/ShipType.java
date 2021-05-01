package battleship;

/**
 * Types of ships.
 */
public enum ShipType {
    CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String NAME;
    private final int SIZE;

    /**
     * Constructor for each ShipType.
     *
     * @param name full name of ShipType
     * @param size the required length of ShipType
     */
    ShipType(String name, int size) {
        this.NAME = name;
        this.SIZE = size;
    }

    /**
     * Returns the full name of the ShipType
     *
     * @return name
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns the length of the ShipType
     *
     * @return size
     */
    public int getSize() {
        return SIZE;
    }
}
