package battleship;

/**
 * Ships that can be placed on a GameField.
 */
public class Ship {
    final ShipType type;
    final int[][] location;
    int units;

    /**
     * Constructor for each Ship.
     * @param type enum for what type of ship it is.
     */
    Ship(ShipType type) {
        this.type = type;
        location = new int[type.getSize()][2];
        units = type.getSize();
    }

    /**
     * Stores the location of the ship on the specified field in a 2D Array of ints.
     * Example: if ships location is A1-D1, array would look like [[0,0],[1,0],[2,0][3,0]]
     * @param i the specific unit of the ship
     * @param y y-location of the unit of the ship
     * @param x x-location of the unit of the ship
     */
    void setLocation(int i, int y, int x) {
        location[i][0] = y;
        location[i][1] = x;
    }

    /**
     * Method checks if shot will sink ship.
     * If ship will be sunk, userAfloat and enemyAfloat will be updated to false respectively.
     * @param y y-coordinate of shot fired
     * @param x x-coordinate of shot fired
     * @param player the player whose ships are being shot at
     * @return true if ship has been sunk
     */
    public static boolean hitShip(int y, int x, Player player) {
        for (Ship ship : player.ships) {
            for (int[] unit : ship.location) {
                if (unit[0] == y && unit[1] == x) {
                    return --ship.units <= 0;
                }
            }
        }
        return false;
    }
}
