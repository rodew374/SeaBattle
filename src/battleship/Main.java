package battleship;

import java.util.Scanner;
import java.util.Arrays;

/**
 * This Main class runs the game Battleship
 * @version 1.14 23 Apr 2021 Version changes displayField() to accept a fogOfWar boolean
 * Version also displays to versions of the field, one hidden by Fog of War and one normal
 * @author Zac Inman
 */
public class Main {
    public static void main(String[] args) {
        new Game().startGame();
    }

    static class Game {
        private final int SIZE = 10;
        private final Scanner scanner = new Scanner(System.in);
        private final char[][] playerField;

        public Game() {
            playerField = createField();

        }

        /**
         * Creates a play area covered by the fog of war
         * @return play area, used for both player and enemy fields
         */
        private char[][] createField() {
            char[][] field = new char[SIZE][SIZE];

            for (char[] row : field) {
                Arrays.fill(row, '~');
            }
            return field;
        }

        /**
         * Initiates the battle
         */
        public void startGame() {
            displayField(playerField, false);
            takePosition();

            System.out.println("\nThe game starts!");
            displayField(playerField, true);
            takeShot(playerField);
        }

        /**
         * Displays the play area to the user
         * @param field can either be a player or enemy field
         * @param fogOfWar determines if ship locations are shown, shown if false
         */
        private void displayField(char[][] field, boolean fogOfWar) {
            String[] labelX = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            char[] labelY = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'j'};

            System.out.printf("\n  %s", String.join(" ", labelX));

            for (int i = 0; i < SIZE; i++) {
                System.out.printf("\n%c ", labelY[i]);

                for (char e : field[i]) {
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
         * Instantiates the ships
         */
        private void takePosition() {
            String prompt = "\nEnter the coordinates of the %s (%d cells):\n\n> ";

            for (Ship ship : Ship.values()) {
                System.out.printf(prompt, ship.getName(), ship.getSize());
                setShip(ship);
                displayField(playerField, false);
            }
        }

        /**
         * Sets position of the ships
         * @param ship enum to be set
         */
        private void setShip(Ship ship) {
            boolean isHorizontal;
            boolean isVertical;
            char startY;
            char endY;
            int startX;
            int endX;
            int fieldIndex;
            int length;
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
                if (length == ship.getSize()) {
                    fieldIndex = startX < endX ? startX - 1 : endX - 1;
                    if (isLegal(startY, startX, length, fieldIndex, true)) {
                        for (int i = 0; i < ship.getSize(); i++) {
                            playerField[startY - 'A'][fieldIndex++] = 'O';
                        }
                    } else {
                        System.out.print("\nError! You placed it too close to another one. Try again:\n\n> ");
                        setShip(ship);
                    }
                } else {
                    System.out.printf("\nError! Wrong length of the %s! Try again:\n\n> ", ship.getName());
                    setShip(ship);
                }
            } else if (isVertical) {
                length = Math.abs(startY - endY) + 1;
                if (length == ship.getSize()) {
                    fieldIndex = startY < endY ? startY - 'A' : endY - 'A';
                    if (isLegal(startY, startX, length, fieldIndex, false)) {
                        for (int i = 0; i < ship.getSize(); i++) {
                            playerField[fieldIndex++][startX - 1] = 'O';
                        }
                    } else {
                        System.out.print("\nError! You placed it too close to another one. Try again:\n\n> ");
                        setShip(ship);
                    }
                } else {
                    System.out.printf("\nError! Wrong length of the %s! Try again:\n\n> ", ship.getName());
                    setShip(ship);
                }
            } else {
                System.out.print("\nError! Wrong ship location! Try again:\n\n> ");
                setShip(ship);
            }
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
         * Checks is a ship can be placed at the specified location
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
                    if (playerField[startY - 'A'][fieldIndex - 1] == 'O') {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ignored) {

                }
                for (int i = 0; i < length; i++) {
                    if (playerField[startY - 'A'][fieldIndex++] == 'O') {
                        return false;
                    }
                }
                try {
                    if (playerField[startY - 'A'][fieldIndex] == 'O') {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ignored) {

                }
            } else {
                try {
                    if (playerField[fieldIndex - 1][startX - 1] == 'O') {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ignored) {

                }
                for (int i = 0; i < length; i++) {
                    if (playerField[fieldIndex++][startX - 1] == 'O') {
                        return false;
                    }
                }
                try {
                    if (playerField[fieldIndex][startX - 1] == 'O') {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ignored) {

                }
            }
            return true;
        }

        /**
         * Gathers coordinate from the user and shoots at the passed war field
         * @param field area to be shot at
         */
        private void takeShot(char[][] field) {
            String[] coord;
            String message;
            char y;
            int x;
            char target;

            System.out.print("\nTake a shot!\n\n> ");

            coord = getCoord();
            y = coord[0].charAt(0);
            x = Integer.parseInt(coord[1]);

            try {
                target = field[y - 'A'][x - 1];
                if (target == 'O') {
                    field[y - 'A'][x - 1] = 'X';
                    message = "You hit a ship!";
                } else if (target == 'X') {
                    message = "You've already hit that target!";
                } else {
                    field[y - 'A'][x - 1] = 'M';
                    message = "You missed!";
                }
                displayField(field, true);
                System.out.printf("\n%s\n", message);
                displayField(field, false);
            } catch (IndexOutOfBoundsException e) {
                System.out.print("\nError! You entered the wrong coordinates! Try again:\n ");
                takeShot(field);
            }
        }
    }
}