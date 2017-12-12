package Application.Model;

import Application.Constants;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Application.Constants.NUM_COLS;
import static Application.Constants.NUM_ROWS;

/**
 * This class represents a possible connect 4 board using a 2D array to represent
 * the board locations.
 */
public class Board {

    private static BoardLines boardLines = new BoardLines();
    private Disc[][] boardValues;
    private int totalMoves;

    /**
     * Constructor
     */
    public Board() {
        boardValues = new Disc[NUM_COLS][NUM_ROWS];
        totalMoves = 0;
    }

    /**
     * Constructor
     *
     * @param board copies the existing board into this instance.
     */
    public Board(Board board) {
        this.boardValues = new Disc[NUM_COLS][NUM_ROWS];
        for (int col = 0; col < NUM_COLS; col++) {
            boardValues[col] = Arrays.copyOf(board.boardValues[col], board.boardValues[col].length);
        }
        this.totalMoves = board.getTotalMoves();
    }

    /**
     * Returns the total moves value.
     *
     * @return the total moves that led to this board layout.
     */
    public int getTotalMoves() {
        return totalMoves;
    }

    /**
     * Adds a disc to the board.
     *
     * @param player the player who placed the disc.
     * @param column the column to place the disc.
     * @param row    the row to place the disc.
     * @return returns the disc that was added.
     */
    public Disc performMove(int player, int column, int row) {
        this.totalMoves++;
        boolean userMove = player == Constants.USER_MOVE;
        return boardValues[column][row] = new Disc(userMove);
    }

    /**
     * Checks for a user win, computer win, draw, or if the game is still in progress.
     *
     * @return a double constant representing a user win, computer win, draw, or still in progress.
     */
    public double checkStatus() {
        double winner = checkForWinInLine(boardLines.getHorizontalLines());
        if (winner != Constants.IN_PROGRESS) {
            return winner;
        }

        winner = checkForWinInLine(boardLines.getVerticalLines());
        if (winner != Constants.IN_PROGRESS) {
            return winner;
        }

        winner = checkForWinInLine(boardLines.getUpwardDiagonalLines());
        if (winner != Constants.IN_PROGRESS) {
            return winner;
        }

        winner = checkForWinInLine(boardLines.getDownwardDiagonalLines());
        if (winner != Constants.IN_PROGRESS) {
            return winner;
        }

        if (findPossibleActions().isEmpty()) {
            return Constants.DRAW_SCORE;
        } else {
            return Constants.IN_PROGRESS;
        }
    }

    /**
     * Returns a list of all possible actions, represented as the column numbers a disc can be placed.
     *
     * @return a list of all possible actions.
     */
    public List<Integer> findPossibleActions() {
        List<Integer> possibleColumns = new ArrayList<>();
        for (Integer column = 0; column < NUM_COLS; column++) {
            // Check if the top position is empty
            if (isValidColumn(column)) {
                possibleColumns.add(column);
            }
        }
        return possibleColumns;
    }

    /**
     * Denotes whether a move can be played into a given column by checking if the column is full.
     *
     * @param column an int, the column number to check if it is full
     * @return true if a disc can be placed into the column, false if the column is full
     */
    private boolean isValidColumn(int column) {
        return boardValues[column][0] == null;
    }

    /**
     * Provides the next empty row in a particular column that a disc can be placed into.
     *
     * @param column an int, the column number to find the next empty position
     * @return an int, the next empty row position
     */
    public int getNextEmptyRow(int column) {
        // Find the lowest row to place the disc
        int row = NUM_ROWS - 1;
        do {
            if ((boardValues[column][row] == null)) {
                break;
            }
            row--;
        } while (row >= 0);
        return row;
    }

    /**
     * Gets a disc from a particular column and row position in the discBoard.
     *
     * @param column an int, the column number to obtain the disc
     * @param row    an int, the row number to obtain the disc
     * @return the disc stored at the current position or empty
     */
    public Disc getDisc(int column, int row) {
        if (column < 0 || column >= NUM_COLS || row < 0 || row >= NUM_ROWS) {
            return null;
        }
        return boardValues[column][row];
    }

    /**
     * Checks for 4 tokens from the same player in a row.
     *
     * @param lines the lines to check to see if a line is filled with the same player's tokens.
     * @return the user win constant for a user win, the computer win constant for a computer win
     * else the in progress constant.
     */
    private double checkForWinInLine(List<List<Point2D>> lines) {
        int chain;
        boolean userMove;

        // Check for game winners
        for (List<Point2D> line : lines) {
            userMove = false;
            chain = 0;
            for (Point2D point : line) {
                int col = (int) point.getX();
                int row = (int) point.getY();
                Disc disc = boardValues[col][row];
                if (disc != null && disc.getMove() == userMove) {
                    chain++;
                    if (chain == 4) {
                        return userMove ? Constants.USER_WIN : Constants.COMP_WIN;
                    }
                } else {
                    if (disc != null) {
                        userMove = disc.getMove();
                        chain = 1;
                    } else {
                        chain = 0;
                    }
                }
            }
        }
        return Constants.IN_PROGRESS;
    }

    /**
     * To String method to postMCTSDisplay a board.
     *
     * @return a string represented the current board.
     */
    @Override
    public String toString() {
        String string = "";
        for (int row = 0; row < Constants.NUM_ROWS; row++) {
            string += "\nRow " + row + ": ";
            for (int col = 0; col < Constants.NUM_COLS; col++) {
                if (getDisc(col, row) != null) {
                    if (getDisc(col, row).getMove()) {
                        string += "  " + 'U' + "  ";
                    } else {
                        string += "  " + 'C' + "  ";
                    }
                } else {
                    string += "  " + col + "  ";
                }
            }
        }
        string += "\n";
        return string;
    }

    /**
     * Checks if two board are equal.
     *
     * @param o the board to check.
     * @return true if the boards are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board = (Board) o;
        for (int column = 0; column < boardValues.length; column++) {
            for (int row = 0; row < boardValues[column].length; row++) {
                if (board.boardValues[column][row] == null && boardValues[column][row] != null) {
                    return false;
                } else if (board.boardValues[column][row] != null && boardValues[column][row] == null) {
                    return false;
                } else if (board.boardValues[column][row] != null && boardValues[column][row] != null) {
                    if (board.boardValues[column][row].getMove() != boardValues[column][row].getMove()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
