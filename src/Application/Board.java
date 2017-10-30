package Application;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Application.Constants.NUM_COLS;
import static Application.Constants.NUM_ROWS;

public class Board {

    private static BoardLines boardLines = new BoardLines();
    private static List<List<Point2D>> horizontalLines = boardLines.getHorizontalLines();
    private static List<List<Point2D>> verticalLines = boardLines.getVerticalLines();
    private static List<List<Point2D>> upwardDiagonalLines = boardLines.getUpwardDiagonalLines();
    private static List<List<Point2D>> downwardDiagonalLines = boardLines.getDownwardDiagonalLines();

    private Disc[][] boardValues;
    private int totalMoves;


    public Board() {
        boardValues = new Disc[NUM_COLS][NUM_ROWS];
        totalMoves = 0;
    }

    public Board(Board board) {
        this.boardValues = new Disc[NUM_COLS][NUM_ROWS];
        for (int col = 0; col < NUM_COLS; col++) {
            boardValues[col] = Arrays.copyOf(board.boardValues[col], board.boardValues[col].length);
        }
        this.totalMoves = board.getTotalMoves();
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public Disc[][] getBoardValues() {
        return boardValues.clone();
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public void setBoardValues(Disc[][] boardValues) {
        this.boardValues = boardValues.clone();
    }

    public Disc performMove(int player, int column, int row) {
        this.totalMoves++;
        boolean userMove = player == Constants.USER_MOVE;
        return boardValues[column][row] = new Disc(userMove);
    }

    public int checkStatus() {
        int winner = checkForWinInLine(horizontalLines);
        if (winner != Constants.IN_PROGRESS) {
            // DEBUG
            System.out.println("Horizontal Winner");
            return winner;
        }

        winner = checkForWinInLine(verticalLines);
        if (winner != Constants.IN_PROGRESS) {
            // DEBUG
            System.out.println("Vertical Winner");
            return winner;
        }

        winner = checkForWinInLine(upwardDiagonalLines);
        if (winner != Constants.IN_PROGRESS) {
            // DEBUG
            System.out.println("Upward Diagonal Winner");
            return winner;
        }

        winner = checkForWinInLine(downwardDiagonalLines);
        if (winner != Constants.IN_PROGRESS) {
            // DEBUG
            System.out.println("Downward Diagonal Winner");
            return winner;
        }

        if (findPossibleColumns().isEmpty()) {
            return Constants.DRAW_SCORE;
        } else {
            return Constants.IN_PROGRESS;
        }
    }


    public List<Integer> findPossibleColumns() {
        List<Integer> possibleColumns = new ArrayList<>();
        for (Integer column = 0; column < NUM_COLS; column++) {
            // Check if the top position is empty
            if (boardValues[column][0] == null) {
                possibleColumns.add(column);
            }
        }
        // DEBUG
        //System.out.print(possibleColumns);
        return possibleColumns;
    }

    /**
     * Provides the next empty row in a particular column that a disc can be placed into.
     *
     * @param column an int, the column number to find the next empty position
     * @return an int, the next empty row position
     */
    public int getNextEmptyRow(int column) {
        // Find the lowest row to place the disc
        // DEBUG
        // System.out.print("getNextEmptyRow ");
        int row = NUM_ROWS - 1;
        do {
            // DEBUG
            // System.out.println("Col: " + column + " Row: " + row);
            if ((boardValues[column][row] == null)) {
                break;
            }
            row--;
        } while (row >= 0);
        // DEBUG
        //System.out.println(row);
        return row;
    }

    /**
     * Denotes whether a move can be played into a given column by checking if the column is full.
     *
     * @param column an int, the column number to check if it is full
     * @return true if a disc can be placed into the column, false if the column is full
     */
    boolean validColumn(int column) {
        return boardValues[column][0] == null;
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

    public int checkForWinInLine(List<List<Point2D>> lines) {
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

    @Override
    public String toString() {
        String string = "";
        for(int row = 0; row < Constants.NUM_ROWS; row++) {
            string += "\nRow " + row + ": ";
            for(int col = 0; col < Constants.NUM_COLS; col++) {
                if(getDisc(col, row) != null) {
                    if(getDisc(col, row).getMove()) {
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
}
