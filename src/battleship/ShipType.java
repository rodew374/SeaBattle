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
     * @param name full name of ShipType
     * @param size the required length of ShipType
     */
    ShipType(String name, int size) {
        this.NAME = name;
        this.SIZE = size;
    }

    /**
     * Returns the full name of the ShipType
     * @return name
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns the length of the ShipType
     * @return size
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Stores the location of the ship on the specified field in a 2D Array of ints.
     * Example: if ships location is A1-D1, array would look like [[0,0],[1,0],[2,0][3,0]]
     * @param i the specific unit of the ship
     * @param y y-location of the unit of the ship
     * @param x x-location of the unit of the ship
     * @param isUser denotes if the ship is on the playerField or the enemyField
     */
    public void setLoc(int i, int y, int x, boolean isUser) {
        if (isUser) {
            userLoc[i][0] = y;
            userLoc[i][1] = x;
        } else {
            enemyLoc[i][0] = y;
            enemyLoc[i][1] = x;
        }
    }

    /**
     * Method checks if shot will sink ship.
     * If ship will be sunk, userAfloat and enemyAfloat will be updated to false respectively.
     * @param y y-coordinate of shot fired
     * @param x x-coordinate of shot fired
     * @param field the playing field the ship fired upon is located on.
     * @return true if ship has been sunk
     */
    public static boolean hitShip(int y, int x, char[][] field) {
        for (ShipType ship : ShipType.values()) {
            for (int[] unit : ship.userLoc) {
                if (unit[0] == y && unit[1] == x) {
                    return checkStatus(ship, field);
                }
            }
        }
        return false;
    }

    /**
     * Helper method for hitShip(int y, int x, char[][] field).
     * @param ship ship that was fired upon
     * @param field the playing field the ship fired upon is located on.
     * @return true if ship has been sunk.
     */
    public static boolean checkStatus(ShipType ship, char[][] field) {
        for (int i = 0; i < ship.SIZE; i++) {
            if (field[ship.userLoc[i][0]][ship.userLoc[i][1]] == 'O') {
                return false;
            }
        }
        ship.userAfloat = false;
        return true;
    }
}
