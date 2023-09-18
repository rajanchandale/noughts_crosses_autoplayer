package java_auto_player;

import java.util.Scanner;

/**
 * Class representing the Noughts and Crosses game
 */
public class NoughtsCrossesGame {

    // Constants representing the various outcomes of a game
    private static final int DIRECT_WIN = 0;
    private static final int WIN = 1;
    private static final int LOSE = 2;
    private static final int DRAW = 3;
    // The current state of the game grid
    private NoughtsCrossesGrid grid;

    /**
     * Default constructor to initialise the game with an empty 3x3 grid
     */
    public NoughtsCrossesGame() {
        this.grid = new NoughtsCrossesGrid();
    }

    /**
     * Copies the current game state
     * 
     * @return a new NoughtsCrossesGame with the current grid state
     */
    public NoughtsCrossesGame copyGame() {
        return new NoughtsCrossesGame(this.grid.copyGrid());
    }

    /**
     * Create a copy of the game with a given grid
     * 
     * @param grid The grid state to be copied
     */
    private NoughtsCrossesGame(NoughtsCrossesGrid grid) {
        this.grid = grid;
    }

    /**
     * Determines the best move for the provided symbol and updates the game state
     * 
     * @param symbol The current player's symbol as a character ('X' or 'O')
     * @return An integer representing the outcome of the best move
     */
    public int determineBestMove(char symbol) {
        if (grid.isGridFull()) {
            return DRAW;
        }

        int[] moves = { -1, -1, -1, -1 };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!grid.isCellEmpty(col, row)) {
                    continue;
                }

                int outcome = simulateMove(col, row, symbol);
                if (outcome == DIRECT_WIN) {
                    grid.makeMove(col, row, symbol);
                    return WIN;
                }

                if (moves[outcome] == -1) {
                    moves[outcome] = row * 3 + col;
                }
            }
        }

        for (int outcome : new int[] { WIN, DRAW, LOSE }) {
            if (moves[outcome] != -1) {
                int col = moves[outcome] % 3;
                int row = moves[outcome] / 3;
                grid.makeMove(col, row, symbol);
                return outcome;
            }
        }

        return DRAW;
    }

    /**
     * Simulates the outcome of a move on the grid
     * 
     * @param col The column coordinate of the move
     * @param row The row coordinate of the move
     * @param symbol The player's symbol
     * @return An integer representing the outcome of the move
     */
    public int simulateMove(int col, int row, char symbol) {
        NoughtsCrossesGame simulatedGame = copyGame();
        simulatedGame.grid.makeMove(col, row, symbol);

        if (simulatedGame.grid.hasPlayerWon(symbol)) {
            return DIRECT_WIN;
        }

        int opponentResult = simulatedGame.determineBestMove(switchPlayer(symbol));
        if (opponentResult == WIN) {
            return LOSE;
        } else if (opponentResult == LOSE) {
            return WIN;
        }
        return DRAW;
    }

    /**
     * Prompts the user to enter the coordinates of their move
     * 
     * @return An array of size 2, first element is the column and the second element is the row
     */
    public int[] getUserMove() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter Column: ");
            int column = scanner.nextInt();
            System.out.print("Enter Row: ");
            int row = scanner.nextInt();

            if (1 <= column && column <= 3 && 1 <= row && row <= 3 && grid.isCellEmpty(column - 1, row - 1)) {
                return new int[] { column - 1, row - 1 };
            }

            System.out.println("Invalid Co-ordinates. Try Again");
        }
    }

    /**
     * Manages the game flow, alternating between the user and the AI autoplayer
     */
    public void playGame() {
        grid.displayGrid();
        while (!grid.isGridFull()) {
            int[] userMove = getUserMove();
            grid.makeMove(userMove[0], userMove[1], 'X');
            System.out.println("\nX PLAYS:");
            grid.displayGrid();

            if (grid.hasPlayerWon('X')) {
                System.out.println("\nX WINS!");
                return;
            }

            int result = determineBestMove('O');
            System.out.println("\nO PLAYS:");
            grid.displayGrid();

            if (result == WIN) {
                System.out.println("\nO WINS!");
                return;
            }
        }

        System.out.println("\nWE DREW!");
    }

    /**
     * Switches the current player's symbol
     * 
     * @param player The current player's symbol
     * @return The other player's symbol
     */
    public static char switchPlayer(char player) {
        return (player == 'X') ? 'O' : 'X';
    }

    /**
     * The entry point of the application
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        NoughtsCrossesGame game = new NoughtsCrossesGame();
        game.playGame();
    }
}