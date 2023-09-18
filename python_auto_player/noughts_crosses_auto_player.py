from copy import deepcopy

DIRECT_WIN = 0
WIN = 1
LOSE = 2
DRAW = 3


class NoughtsCrossesGrid:
    """Class representing a Noughts & Crosses Grid"""
    def __init__(self):
        """Initialises an empty grid"""
        self.clear_grid()

    def clear_grid(self):
        """Clears the grid to its initial state"""
        self.grid = [['_' for col in range(3)] for row in range(3)]
        self.total_moves = 0

    def display_grid(self):
        """Displays the current state of the Noughts & Crosses grid to the console"""
        for row in self.grid:
            for cell in row:
                print(cell + " ", end="")
            print("\n")    
        

    def configure_grid(self, *rows):
        """
        Configures the grid using specified row data
        
        Parameters:
            - *rows (tuple): Each argument should be a string representing a row. E.g: "X_O", "_X_", "O_O" etc
        """
        self.total_moves = sum([row.count('X') + row.count('O') for row in rows])
        for i, row in enumerate(rows):
            self.grid[i] = list(row)

    def is_grid_full(self):
        """Returns True if the grid is full, otherwise False. Based on a standard 3x3 Noughts & Crosses grid"""
        return self.total_moves >= 9

    def has_player_won(self, symbol):
        """
        Checks if the specified player has won by checking rows, columns, and diagonals
        
        Parameters: 
            - symbol (str): Player symbol, either 'X' or 'O'
            
        Returns:
            - bool: True if the player has won, otherwise False
        """
        for i in range(3):
            if all([cell == symbol for cell in self.grid[i]]) or all([self.grid[j][i] == symbol for j in range(3)]):
                return True

        return all([self.grid[i][i] == symbol for i in range(3)]) or all([self.grid[i][2-i] == symbol for i in range(3)])

    def make_move(self, col, row, symbol):
        """
        Simply marks the specified cell in the 3x3 grid with the player's symbol
        
        Parameters:
            - col (int): Integer column coordinate in 3x3 grid for the player's move
            - row (int): Integer row coordinate in 3x3 grid for the player's move
            - symbol (str): Player symbol, either 'X' or 'O'
        """
        self.grid[row][col] = symbol
        self.total_moves += 1

    def is_cell_empty(self, col, row):
        """
        Checks if the specified cell is empty
        
        Parameters:
            - col (int): Integer column coordinate in 3x3 grid to be checked 
            - row (int): Integer row coordinate in 3x3 grid to be checked
        """
        return self.grid[row][col] == '_'


def switch_player(player):
    """Helper function to switch the current symbol making a move"""
    return 'O' if player == 'X' else 'X'


class NoughtsCrossesGame:
    """Class representing a Noughts & Crosses game"""
    def __init__(self):
        """Initialises a new game with an empty grid"""
        self.grid = NoughtsCrossesGrid()

    def copy_game(self):
        """Creates and returns a deep copy of the current game"""
        return deepcopy(self)

    def determine_best_move(self, symbol):
        """
        Determines the best move for the specified player using a simple AI algorithm
        
        Parameters:
            - symbol (str): Player symbol, either 'X' or 'O'
            
        Returns:
            - int: Outcome of the game (WIN, LOSE, DRAW, or DIRECT_WIN)
        """
        # Check if the grid is full, indicating a draw
        if self.grid.is_grid_full():
            return DRAW

        # Dict to store potential moves & outcomes
        moves = {
            DIRECT_WIN: None,
            WIN: None,
            DRAW: None,
            LOSE: None
        }

        #Iterate over all the cells in the grid
        for row in range(3):
            for col in range(3):
                # If the cell has already been played, skip it
                if not self.grid.is_cell_empty(col, row):
                    continue

                # Simulate the outcome of making a move at the current cell
                outcome = self.simulate_move(col, row, symbol)
                
                # If the move results in a direct win, make the move and return WIN
                if outcome == DIRECT_WIN:
                    self.grid.make_move(col, row, symbol)
                    return WIN

                # Store the move's outcome for later decision-making
                if outcome in moves:
                    moves[outcome] = [col, row]

        # Attempt to find the best move based on the stored outcomes
        for outcome in [WIN, DRAW, LOSE]:
            if moves[outcome]:
                self.grid.make_move(moves[outcome][0], moves[outcome][1], symbol)
                return outcome

        # Default return value indicating a draw
        return DRAW

    def simulate_move(self, col, row, symbol):
        """
        Simulates a move and determines its outcome.
        
        Parameters:
            - col (int): Integer column coordinate in 3x3 grid for the potential move
            - row (int): Integer row coordinate in a 3x3 grid for the potential move
            - symbol (str): Player symbol, either 'X' or 'O'
            
        Returns:
            - int: Outcome of the move (WIN, LOSE, DRAW, or DIRECT_WIN)
        """
        # Create a copy of the current game state to simulate the move without affecting the actual game
        simulated_game = self.copy_game()
        simulated_game.grid.make_move(col, row, symbol)

        # Check if the simulated move results in a win
        if simulated_game.grid.has_player_won(symbol):
            return DIRECT_WIN

        # Determine the best move for the opponent based on the simulated game state
        opponent_result = simulated_game.determine_best_move(switch_player(symbol))
        
        # Interpret the opponent's result and return the corresponding outcome for the current player
        if opponent_result == WIN:
            return LOSE # If the opponent wins, autoplayer loses
        elif opponent_result == LOSE:
            return WIN # If the opponent loses, the autoplayer wins
        # Default return value indicating a draw
        return DRAW

    def get_user_move(self):
        """Prompts the user for the coordinates of a new move until a valid move is entered"""
        while True:
            column = int(input("\nEnter Column: "))
            row = int(input("Enter Row: "))

            if 1 <= column <= 3 and 1 <= row <= 3 and self.grid.is_cell_empty(column-1, row-1):
                return column-1, row-1

            print("Invalid Co-ordinates. Try Again")

    def play_game(self):
        """Manages the game flow, alternating between the user and the AI autoplayer"""
        self.grid.display_grid()
        while not self.grid.is_grid_full():
            user_move_col, user_move_row = self.get_user_move()
            self.grid.make_move(user_move_col, user_move_row, 'X')
            print("\nX PLAYS:")
            self.grid.display_grid()

            if self.grid.has_player_won('X'):
                print("\nX WINS!")
                return

            result = self.determine_best_move('O')
            print("\nO PLAYS:")
            self.grid.display_grid()

            if result == WIN:
                print("\nO WINS!")
                return

        print("\nWE DREW!")


if __name__ == '__main__':
    # Entry Point
    game = NoughtsCrossesGame()
    game.play_game()
