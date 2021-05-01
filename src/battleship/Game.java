package battleship;

/**
 * This Game class runs the game Battleship
 * @version 2.0 30 Apr 2021 Version implements better abstraction and game is now two-player.
 * @author Zac Inman
 */
public class Game {
    private final Player player1;
    private final Player player2;

    /**
     * Entry point to the program
     * @param args default
     */
    public static void main(String[] args) {
        Game game = new Game();

        game.setUp();
        game.start();
    }

    /**
     * Constructor for a 2-Person game of Battleship
     */
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
        Player.changePlayerPrompt();
        player2.takePosition();
    }

    /**
     * Starts the game.
     * Player one starts by entering coordinates of first shot. Player two will go next.
     * Game continues until a player has sunk all their opponents ships.
     */
    private void start() {
        player1.startBattle();
    }
}