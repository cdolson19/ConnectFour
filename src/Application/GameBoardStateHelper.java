package Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Application.Constants.NUM_COLS;
import static Application.Constants.NUM_ROWS;

public class GameBoardStateHelper {

    public static List<Integer> findPossibleColumns(Disc[][] grid) {
        List<Integer> possibleColumns = new ArrayList<>();
        for(Integer column = 0; column < NUM_COLS; column++) {
            // Check if the top position is empty
            if(grid[column][0] == null) {
                possibleColumns.add(column);
            }
        }
        // DEBUG
        System.out.print(possibleColumns);
        return possibleColumns;
    }

    /**
     * Provides the next empty row in a particular column that a disc can be placed into.
     * @param column an int, the column number to find the next empty position
     * @return an int, the next empty row position
     */
    static int getNextEmptyRow(int column, Disc[][] grid) {
        // Find the lowest row to place the disc
        // DEBUG
        System.out.print("getNextEmptyRow ");
        int row = NUM_ROWS - 1;
        do {
            if ((grid[column][row] == null)) {
                break;
            }
            row--;
        } while (row >= 0);
        // DEBUG
        System.out.println(row);
        return row;
    }

    public static boolean rollout(Disc[][] grid, boolean userMove) {
        // Get the column to place the next disc randomly
        List<Integer> possibleColumns = GameBoardStateHelper.findPossibleColumns(grid);
        Random random = new Random();
        int columnIndex = random.nextInt(possibleColumns.size());
        int column = possibleColumns.get(columnIndex);
        // Get the row to place the next disc
        int row = GameBoardStateHelper.getNextEmptyRow(column, grid);
        grid[column][row] = new Disc(userMove);
        // Check for game over
        if(GameBoardController.gameWon(column, row, grid, userMove)) {
            return userMove;
        }
        return rollout(grid, !userMove);
    }
}
