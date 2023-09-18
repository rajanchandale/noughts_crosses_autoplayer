package java_auto_player;

import java.util.Arrays;

/**
 * Represents the grid for the Noughts & Crosses game
 */
public class NoughtsCrossesGrid {

    // 2D array to represent the game grid
    private char[][] grid;
    // Keeps track of total moves made on the grid
    private int totalMoves;

    /**
     * Default constructor that initialises the grid to an empty state
     */
    public NoughtsCrossesGrid() {
        clearGrid();
    }

    /**
     * Creates a deep copy of the current grid
     * 
     * @return A new instance of NoughtsCrossesGrid with the current grid's state
     */
    public NoughtsCrossesGrid copyGrid() {
        char[][] newGrid = new char[3][3];
        for (int i = 0; i < 3; i++) {
            newGrid[i] = Arrays.copyOf(grid[i], 3);
        }
        NoughtsCrossesGrid newNCG = new NoughtsCrossesGrid();
        newNCG.grid = newGrid;
        newNCG.totalMoves = this.totalMoves;
        return newNCG;
    }

    /**
     * Resets the grid to its initial empty state
     */
    public void clearGrid() {
        grid = new char[3][3];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(grid[i], '_');
        }
        totalMoves = 0;
    }

    /**
     * Displays the current state of the grid on the console
     */
    public void displayGrid() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Checks if the 3x3 grid is full, i.e., all cells are occupied
     * 
     * @return true if the grid is full, otherwise false
     */
    public boolean isGridFull() {
        return totalMoves >= 9;
    }

    /**
     * Determines if a player with the specified symbol has won the game
     * 
     * @param symbol The player's symbol ('X' or 'O')
     * @return true if the player has won, otherwise false
     */
    public boolean hasPlayerWon(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (allSame(grid[i], symbol) || allSame(new char[] { grid[0][i], grid[1][i], grid[2][i] }, symbol)) {
                return true;
            }
        }

        return allSame(new char[] { grid[0][0], grid[1][1], grid[2][2] }, symbol) ||
               allSame(new char[] { grid[0][2], grid[1][1], grid[2][0] }, symbol);
    }

    /**
     * Places a symbol on the specified cell of the grid
     * 
     * @param col The column of the cell
     * @param row The row of the cell
     * @param symbol The player's symbol ('X' or 'O')
     */
    public void makeMove(int col, int row, char symbol) {
        grid[row][col] = symbol;
        totalMoves++;
    }

    /**
     * Checks if the specified cell is empty
     * 
     * @param col The column of the cell
     * @param row The row of the cell
     * @return true if the cell is empty, false otherwise
     */
    public boolean isCellEmpty(int col, int row) {
        return grid[row][col] == '_';
    }

    /**
     * Checks if all elements of a char array are the same as a specified value
     * 
     * @param arr The char array
     * @param value The value to compare against
     * @return true if all elements are the same as the value, otherwise false
     */
    private boolean allSame(char[] arr, char value) {
        for (char c : arr) {
            if (c != value) return false;
        }
        return true;
    }
}