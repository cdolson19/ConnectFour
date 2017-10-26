package Application;

import static Application.Constants.NUM_COLS;
import static Application.Constants.NUM_ROWS;

public class GameBoardState {

    private Disc[][] grid = new Disc[NUM_COLS][NUM_ROWS];

    GameBoardState() {
        grid = new Disc[NUM_COLS][NUM_ROWS];
    }

    GameBoardState(Disc[][] grid) {
        this.grid = grid;
    }
    /**
     * Gets a disc from a particular column and row position in the grid.
     * @param column an int, the column number to obtain the disc
     * @param row an int, the row number to obtain the disc
     * @return the disc stored at the current position or empty
     */
    Disc getDisc(int column, int row) {
        if (column < 0 || column >= NUM_COLS || row < 0 || row >= NUM_ROWS) {
            return null;
        }
        return grid[column][row];
    }

    Disc[][] getGrid() {
        return grid;
    }

    /**
     * Denotes whether a move can be played into a given column by checking if the column is full.
     * @param column an int, the column number to check if it is full
     * @return true if a disc can be placed into the column, false if the column is full
     */
    boolean validColumn(int column) {
        return getDisc(column, 0) == null;
    }

    void insertDisc(int column, int row, Disc disc) {
        grid[column][row] = disc;
    }
}
