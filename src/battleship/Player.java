package battleship;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

class Player {
    private static final int FIELD_SIZE = 10;
    private static final Scanner scanner = new Scanner(System.in);
    final Ship[] ships = {new Ship(ShipType.CARRIER), new Ship(ShipType.BATTLESHIP),
                            new Ship(ShipType.SUBMARINE), new Ship(ShipType.CRUISER),
                                new Ship(ShipType.DESTROYER)};
    int shipsAfloat = 5;
    private final char[][] GAME_FIELD;
    private final int NAME;
    private Player opponent;

    /**
     * Initializes a player with a GAME_FIELD.
     * @param playerNumber the name of the player (can be 1 or 2)
     * @param opponent reference to the other player
     */
    Player(int playerNumber, Player opponent) {
        NAME = playerNumber;
        this.opponent = opponent;
        GAME_FIELD = new char[FIELD_SIZE][FIELD_SIZE];

        for (char[] row : GAME_FIELD) { Arrays.fill(row, '~'); }
    }

    /**
     * Constructor for player 1.
     * Since player 2 has not been created yet, it will pass a null Player to other constructor.
     * @param playerNumber the name of the player
     */
    Player(int playerNumber) {
        this(playerNumber, null);
    }

    /**
     * Sets the other player to the opponent.
     * Should only be used on player 1.
     * @param opponent Player 2
     */
    void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * Players will take turns shooting at each other's GAME_FIELD's.
     * Method will continue until a player's last ship is sunk.
     */
    void startBattle() {
        Player current = opponent;

        while (current.shipsAfloat > 0 && current.opponent.shipsAfloat > 0) {
            changePlayerPrompt();
            current = current.opponent;
            current.opponent.display();
            System.out.print("----------------------");
            current.display(false);
            System.out.printf("\nPlayer %d, Take a shot!\n\n> ", current.NAME);
            current.takeShot();
        }

        System.out.printf("\nYou sank the last ship. Congratulations! Player %d wins!!!", current.NAME);
    }

    /**
     * Prompts the current player to give the controls to their opponent.
     * Player should click enter to pass the turn.
     * Method also prints to the console a 'form feed' (\f) character.
     * Using IntelliJ with the 'Grep Console' plugin, printing \f allows me to clear the console.
     */
    static void changePlayerPrompt() {
        System.out.println("\nPress Enter and pass the move to opponent");

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException ignored) {

        }
        System.out.println("\f");
    }

    /**
     * Reads user input and returns the data as coordinates formatted as an array of Strings
     * The coordinate array consists of:
     *  coord[0]: a letter [A-Z]
     *  coord[1]: a number [1-10]
     * @return an array of Strings
     */
    private String[] getCoord() {

        return scanner.next().toUpperCase().split("", 2);
    }

    /**
     * Displays the GameField to the user.
     * Ships are hidden by the Fog of War.
     */
    private void display() { display(true); }

    /**
     * Displays the GameField to the user.
     * Ships have the option to be hidden by the Fog of War.
     * @param fogOfWar determines if ship locations are hidden, shown if false
     */
    private void display(boolean fogOfWar) {
        String[] X_LABEL = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        char[] Y_LABEL = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'j'};

        System.out.printf("\n  %s", String.join(" ", X_LABEL));

        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.printf("\n%c ", Y_LABEL[i]);

            for (char e : GAME_FIELD[i]) {
                if (fogOfWar && e == 'O') {
                    System.out.print("~ ");
                } else {
                    System.out.printf("%c ", e);
                }
            }
        }
        System.out.println();
    }

    /**
     * Places each of the player's ships onto the GameField by prompting coordinates from the player.
     * Also sets the ship.location for each ship.
     */
    void takePosition() {
        String prompt = "\nEnter the coordinates of the %s (%d cells):\n\n> ";

        System.out.printf("Player %d, place your ships on the game field\n", NAME);
        display(false);

        for (Ship ship : ships) {
            System.out.printf(prompt, ship.type.getName(), ship.type.getSize());
            placeShip(ship);
            display(false);
        }
    }

    /**
     * Helper method for takePosition().
     * @param ship Ship to be placed.
     */
    private void placeShip(Ship ship) {
        boolean isHorizontal;
        boolean isVertical;
        char startY;            // A letter [A-J], representing first half of starting coordinates for the ship to be placed.
        char endY;              // A letter [A-J], representing first half of ending coordinates for the ship to be placed.
        int startX;             // A number [1-10], representing second half of starting coordinates for the ship to be placed.
        int endX;               // A number [1-10], representing second half of ending coordinates for the ship to be placed.
        int fieldIndex;         // Starting index of the FIELD to be checked.
        int length;             // Distance between start and end coordinates + 1 (should be equal to ship SIZE).
        String[] coord;

        coord = getCoord();
        startY = coord[0].charAt(0);
        startX = Integer.parseInt(coord[1]);

        coord = getCoord();
        endY = coord[0].charAt(0);
        endX = Integer.parseInt(coord[1]);

        isHorizontal = startY == endY;
        isVertical = startX == endX;

        if (isHorizontal) {
            length = Math.abs(startX - endX) + 1;
            if (length == ship.type.getSize()) {
                fieldIndex = startX < endX ? startX - 1 : endX - 1;
                if (isLegal(startY, startX, length, fieldIndex, true)) {
                    for (int i = 0; i < ship.type.getSize(); i++) {
                        ship.setLocation(i, startY - 'A', fieldIndex);
                        GAME_FIELD[startY - 'A'][fieldIndex++] = 'O';
                    }
                } else {
                    System.out.print("\nError! You placed it too close to another one. Try again:\n\n> ");
                    placeShip(ship);
                }
            } else {
                System.out.printf("\nError! Wrong length of the %s! Try again:\n\n> ", ship.type.getName());
                placeShip(ship);
            }
        } else if (isVertical) {
            length = Math.abs(startY - endY) + 1;
            if (length == ship.type.getSize()) {
                fieldIndex = startY < endY ? startY - 'A' : endY - 'A';
                if (isLegal(startY, startX, length, fieldIndex, false)) {
                    for (int i = 0; i < ship.type.getSize(); i++) {
                        ship.setLocation(i, fieldIndex, startX - 1);
                        GAME_FIELD[fieldIndex++][startX - 1] = 'O';
                    }
                } else {
                    System.out.print("\nError! You placed it too close to another one. Try again:\n\n> ");
                    placeShip(ship);
                }
            } else {
                System.out.printf("\nError! Wrong length of the %s! Try again:\n\n> ", ship.type.getName());
                placeShip(ship);
            }
        } else {
            System.out.print("\nError! Ship must be placed vertically/horizontally. Try again:\n\n> ");
            placeShip(ship);
        }
    }

    /**
     * Checks if a ship can be placed at the specified location
     * @param startY a letter [A-J], representing first half of starting coordinates for the ship to be placed
     * @param startX a number [1-10], representing second half of starting coordinates for the ship to be placed
     * @param length length of ship to be placed
     * @param fieldIndex starting index of the field area to be checked
     * @param isHorizontal direction the field will be checked
     * @return false if there is already a ship at one of the coordinates and directly in front of or behind, otherwise, true
     */
    private boolean isLegal(char startY, int startX, int length, int fieldIndex, boolean isHorizontal) {
        if (isHorizontal) {
            try {
                if (GAME_FIELD[startY - 'A'][fieldIndex - 1] == 'O') {
                    return false;
                }
            } catch (IndexOutOfBoundsException ignored) {

            }
            for (int i = 0; i < length; i++) {
                if (GAME_FIELD[startY - 'A'][fieldIndex++] == 'O') {
                    return false;
                }
            }
            try {
                if (GAME_FIELD[startY - 'A'][fieldIndex] == 'O') {
                    return false;
                }
            } catch (IndexOutOfBoundsException ignored) {

            }
        } else {
            try {
                if (GAME_FIELD[fieldIndex - 1][startX - 1] == 'O') {
                    return false;
                }
            } catch (IndexOutOfBoundsException ignored) {

            }
            for (int i = 0; i < length; i++) {
                if (GAME_FIELD[fieldIndex++][startX - 1] == 'O') {
                    return false;
                }
            }
            try {
                if (GAME_FIELD[fieldIndex][startX - 1] == 'O') {
                    return false;
                }
            } catch (IndexOutOfBoundsException ignored) {

            }
        }
        return true;
    }

    /**
     * Gathers coordinate from the user and shoots at the passed GAME_FIELD.
     */
    private void takeShot() {
        String[] coord;
        String message;
        char y;
        int x;
        char target;



        coord = getCoord();
        y = coord[0].charAt(0);
        x = Integer.parseInt(coord[1]);

        try {
            target = opponent.GAME_FIELD[y - 'A'][x - 1];
            if (target == 'O') {
                opponent.GAME_FIELD[y - 'A'][x - 1] = 'X';
                if (Ship.hitShip(y - 'A', x - 1, opponent)){
                    message = "You sank a ship!";
                    opponent.shipsAfloat--;
                } else {
                    message = "You hit a ship!";
                }
            } else if (target == 'X') {
                message = "You've already hit that target.";
            } else {
                opponent.GAME_FIELD[y - 'A'][x - 1] = 'M';
                message = "You missed.";
            }

            System.out.printf("\n%s\n", message);

        } catch (IndexOutOfBoundsException e) {
            System.out.print("\nError! You entered the wrong coordinates! Try again:\n\n> ");
            takeShot();
        }
    }
}
