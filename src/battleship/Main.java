package battleship;

import java.util.Scanner;
import java.util.Arrays;

/**
 * This Main class runs the game Battleship
 * @version 1.13 17 Mar 2021 Version fixes setShip(ship, size) and adds isLegal(...)
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

        public void startGame() {
            displayField(playerField);
            takePosition();

        }

        /**
         * Displays the play area to the use
         * @param field can either be a player or enemy field
         */
        private void displayField(char[][] field) {
            String[] labelX = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            char[] labelY = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'j'};

            System.out.printf("\n  %s", String.join(" ", labelX));

            for (int i = 0; i < SIZE; i++) {
                System.out.printf("\n%c ", labelY[i]);

                for (char e : field[i]) {
                    System.out.printf("%c ", e);
                }
            }
            System.out.println();
        }

        private void takePosition() {
            String ship1 = "Aircraft Carrier";
            String ship2 = "Battleship";
            String ship3 = "Submarine";
            String ship4 = "Cruiser";
            String ship5 = "Destroyer";

            String prompt = "\nEnter the coordinates of the %s (%d cells):\n\n> ";

            System.out.printf(prompt, ship1, 5);
            setShip(ship1, 5);
            displayField(playerField);

            System.out.printf(prompt, ship2, 4);
            setShip(ship2, 4);
            displayField(playerField);

            System.out.printf(prompt, ship3, 3);
            setShip(ship3, 3);
            displayField(playerField);

            System.out.printf(prompt, ship4, 3);
            setShip(ship4, 3);
            displayField(playerField);

            System.out.printf(prompt, ship5, 2);
            setShip(ship5, 2);
            displayField(playerField);

        }

        private void setShip(String ship, int size) {
            boolean isHorizontal;
            boolean isVertical;
            char startY;
            char endY;
            int startX;
            int endX;
            int fieldIndex;
            int length;
            String[][] coords;

            coords = getCoords();
            startY = coords[0][0].charAt(0);
            endY = coords[1][0].charAt(0);
            startX = Integer.parseInt(coords[0][1]);
            endX = Integer.parseInt(coords[1][1]);
            isHorizontal = startY == endY;
            isVertical = startX == endX;

            if (isHorizontal) {
                length = Math.abs(startX - endX) + 1;
                if (length == size) {
                    fieldIndex = startX < endX ? startX - 1 : endX - 1;
                    if (isLegal(startY, startX, length, fieldIndex, true)) {
                        for (int i = 0; i < size; i++) {
                            playerField[startY - 'A'][fieldIndex++] = 'O';
                        }
                    } else {
                        System.out.print("\nError! You placed it too close to another one. Try again:\n\n> ");
                        setShip(ship, size);
                    }
                } else {
                    System.out.printf("\nError! Wrong length of the %s! Try again:\n\n> ", ship);
                    setShip(ship, size);
                }
            } else if (isVertical) {
                length = Math.abs(startY - endY) + 1;
                if (length == size) {
                    fieldIndex = startY < endY ? startY - 'A' : endY - 'A';
                    if (isLegal(startY, startX, length, fieldIndex, false)) {
                        for (int i = 0; i < size; i++) {
                            playerField[fieldIndex++][startX - 1] = 'O';
                        }
                    } else {
                        System.out.print("\nError! You placed it too close to another one. Try again:\n\n> ");
                        setShip(ship, size);
                    }
                } else {
                    System.out.printf("\nError! Wrong length of the %s! Try again:\n\n> ", ship);
                    setShip(ship, size);
                }
            } else {
                System.out.print("\nError! Wrong ship location! Try again:\n\n> ");
                setShip(ship, size);
            }
        }

        /**
         * Reads user input and returns the data as coordinates formatted as a 2D array of Strings
         * The 2D coordinates array consists of:
         *  array[0]: start
         *      start[0]: a letter [A-J]
         *      start[1]: a number [1-10]
         *  array[1]: end
         *      end[0]: a letter [A-Z]
         *      end[1]: a number [1-10]
         * @return a 2D array of Strings
         */
        private String[][] getCoords() {
            String[] input = scanner.nextLine().toUpperCase().split(" ");
            String[] start = input[0].split("", 2);
            String[] end = input[1].split("", 2);

            return new String[][] {start, end};
        }

        /**
         * Checks is a ship can be placed at the specified location
         * @param startY a letter [A-J], representing first half of starting coordinates for the ship to be placed
         * @param startX a number [1-10], representing second half of starting coordinates for the ship to be placed
         * @param length length of ship to be placed
         * @param fieldIndex starting index of the field area to be checked
         * @param isHorizontal direction the field will be checked
         * @return false if there is already a ship at one of the coordinates, otherwise, true
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
    }
}
