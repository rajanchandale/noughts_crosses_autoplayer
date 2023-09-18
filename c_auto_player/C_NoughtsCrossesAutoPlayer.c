#include <stdio.h>
#include <stdbool.h>
#include <string.h>

// Define constants for game outcomes
#define WIN_FOR_O 10
#define WIN_FOR_X -10
#define DRAW 0
#define GRID_SIZE 3

// Define the structure of the grid, including the 3x3 matrix and a counter for total moves
typedef struct {
    char grid[GRID_SIZE][GRID_SIZE];
    int total_moves;
} NoughtsCrossesGrid;

// Initialise the grid to a clean state
void init_grid(NoughtsCrossesGrid* grid){
    for (int row = 0; row < 3; row++){
        for (int col = 0; col < 3; col++) {
            grid->grid[row][col] = '_';
        }
    }
    grid->total_moves=0;
}

// Display the current state of the grid
void display_grid(const NoughtsCrossesGrid* grid) {
    for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 3; col++) {
            printf("%c ", grid->grid[row][col]);
        }
        printf("\n");
    }
}

// Check if the grid is full
int is_grid_full(const NoughtsCrossesGrid* grid) {
    return grid->total_moves >= 9;
}

// Check if a player with a given symbol has won the game
int has_player_won(const NoughtsCrossesGrid* grid, char symbol) {
    // Check rows, columns, diagonals
    for (int i = 0; i < 3; i++) {
        if (grid->grid[i][0] == symbol && grid->grid[i][1] == symbol && grid->grid[i][2] == symbol) return 1;
        if (grid->grid[0][i] == symbol && grid->grid[1][i] == symbol && grid->grid[2][i] == symbol) return 1;
    }
    if (grid->grid[0][0] == symbol && grid->grid[1][1] == symbol && grid->grid[2][2] == symbol) return 1;
    if (grid->grid[0][2] == symbol && grid->grid[1][1] == symbol && grid->grid[2][0] == symbol) return 1;

    return 0;
}

// Make a move on the grid with the given symbol
void make_move(NoughtsCrossesGrid* grid, int col, int row, char symbol) {
    grid -> grid[row][col] = symbol;
    grid->total_moves++;
}

// Check if a cell is empty
int is_cell_empty(const NoughtsCrossesGrid* grid, int col, int row) {
    return grid->grid[row][col] == '_';
}

// Switch the player from 'X' to 'O' or vice versa
char switch_player(char player) {
    return (player == 'X') ? 'O' : 'X';
}

// Define game structure containing the grid
typedef struct {
    NoughtsCrossesGrid grid;
} NoughtsCrossesGame;

// Create a copy of the grid from source to destination
void copy_grid(NoughtsCrossesGrid* dest, const NoughtsCrossesGrid* src) {
    for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 3; col++) {
            dest->grid[row][col] = src->grid[row][col];
        }
    }
    dest->total_moves = src->total_moves;
}

//Simulate a move and determine the best move for the autoplayer
int simulate_move(NoughtsCrossesGame* game, int col, int row, char symbol);

// Determine the best moving using a simple AI strategy
int determine_best_move(NoughtsCrossesGame* game, char symbol) {
    int bestOutcome;
    
    if (symbol == 'O') {
        bestOutcome = WIN_FOR_X;  // Initialize with the worst case for O
    } else {
        bestOutcome = WIN_FOR_O;  // Initialize with the worst case for X
    }

    int bestMoveRow = -1, bestMoveCol = -1;

    for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 3; col++) {
            if (is_cell_empty(&game->grid, col, row)) {
                int outcome = simulate_move(game, col, row, symbol);

                if ((symbol == 'O' && outcome > bestOutcome) ||
                    (symbol == 'X' && outcome < bestOutcome)) {
                    bestOutcome = outcome;
                    bestMoveRow = row;
                    bestMoveCol = col;
                }
            }
        }
    }

    if (bestMoveRow != -1 && bestMoveCol != -1) {
        make_move(&game->grid, bestMoveCol, bestMoveRow, symbol);
    }

    return bestOutcome;
}


int simulate_move(NoughtsCrossesGame* game, int col, int row, char symbol) {
    NoughtsCrossesGame simulatedGame;
    copy_grid(&simulatedGame.grid, &game->grid);
    make_move(&simulatedGame.grid, col, row, symbol);

    if (has_player_won(&simulatedGame.grid, symbol)) {
        if (symbol == 'O') {
            return WIN_FOR_O;
        } else {
            return WIN_FOR_X;
        }
    } else if (is_grid_full(&simulatedGame.grid)) {
        return DRAW;
    }

    return determine_best_move(&simulatedGame, switch_player(symbol));
}

// Get the coordinates for the user's move
void get_user_move(int *col, int *row, const NoughtsCrossesGrid* grid) {
    while (1) {
        printf("\nEnter Column (1-3): ");
        scanf("%d", col);

        printf("Enter Row (1-3): ");
        scanf("%d", row);

        (*col)--;
        (*row)--;

        if(*col >= 0 && *col < 3 && *row >= 0 && *row < 3 && is_cell_empty(grid, *col, *row)) {
            break;
        }

        printf("Invalid Co-orinates. Try Again.\n");
    }
}

// Main game loop
void play_game() {
    NoughtsCrossesGame game;
    init_grid(&game.grid);
    display_grid(&game.grid);

    while (!is_grid_full(&game.grid)) {

        int user_move_col, user_move_row;
        get_user_move(&user_move_col, &user_move_row, &game.grid);
        make_move(&game.grid, user_move_col, user_move_row, 'X');

        printf("\nX PLAYS:\n");
        display_grid(&game.grid);

        if (has_player_won(&game.grid, 'X')) {
            printf("\nX WINS!\n");
            return;
        }

        if (!is_grid_full(&game.grid)) {
            determine_best_move(&game, 'O');

            printf("\nO PLAYS:\n");
            display_grid(&game.grid);

            if (has_player_won(&game.grid, 'O')) {
                printf("\nO WINS!\n");
                return;
            }
        }
    }
    printf("\nWE DREW!\n");
}

// Entry Point
int main() {
    play_game();
    return 0;
}