package battleship;

/**
 * This Game class runs the game Battleship
 * @version 1.15 25 Apr 2021 Version Fixes the weird entry point to the program, no longer requiring
 * a static class.
 * @author Zac Inman
 */
public class Game {
    private final Player player1;
    private final Player player2;

    public static void main(String[] args) {
        Game game = new Game();

        game.setUp();
        game.start();
    }

    public Game() {
        player1 = new Player(1);
        player2 = new Player(2, player1);
        player1.setOpponent(player2);
    }

    /**
     * Prompts players to place their ships.
     */
    private void setUp() {
        player1.takePosition();
        player2.takePosition();
    }

    private void start() {
        player1.battle();
    }



    /**
     * Check is all user or enemy ships have been sunk.
     * If either has lost all ships, gameOver boolean will be updated to true.
     */
    private void updateGameStatus() {
        int userShips = 5;
        int enemyShips = 5;

        for (ShipType ship : ShipType.values()) {
            if (!ship.userAfloat) {
                userShips--;
            }
            if (!ship.enemyAfloat) {
                enemyShips--;
            }
        }

        gameOver = userShips == 0 || enemyShips == 0;
    }
}