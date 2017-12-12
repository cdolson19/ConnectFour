package model.environment;

import application.Constants;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * This class initializes the possible lines of 4 points in a row where one of the
 * players could fill to win the game.
 */
public class BoardLines {

    private static List<List<Point2D>> HorizontalLines = new ArrayList<>();
    private static List<List<Point2D>> VerticalLines = new ArrayList<>();
    private static List<List<Point2D>> UpwardDiagonalLines = new ArrayList<>();
    private static List<List<Point2D>> DownwardDiagonalLines = new ArrayList<>();

    /**
     * Constructor, initializes the possible horizontal, vertical, and diagonal lines that can be filled.
     */
    BoardLines() {
        initializeHorizontalPoints();
        initializeVerticalPoints();
        initializeUpwardDiagonalPoints();
        initializeDownwardDiagonalPoints();
    }

    /**
     * Returns the horizontal lines.
     *
     * @return a list of each list of four horizontal points in a row.
     */
    public List<List<Point2D>> getHorizontalLines() {
        return HorizontalLines;
    }

    /**
     * Returns the vertical lines.
     *
     * @return a list of each list of four vertical points in a row.
     */
    public List<List<Point2D>> getVerticalLines() {
        return VerticalLines;
    }

    /**
     * Returns the diagonal lines that are upward sloping.
     *
     * @return a list of each list of upward sloping points in a row.
     */
    public List<List<Point2D>> getUpwardDiagonalLines() {
        return UpwardDiagonalLines;
    }

    /**
     * Returns the diagonal lines that are downward sloping.
     *
     * @return a list of each list of downward sloping points in a row.
     */
    public List<List<Point2D>> getDownwardDiagonalLines() {
        return DownwardDiagonalLines;
    }

    /**
     * Creates a list of all possible lists of four points in a horizontal row.
     */
    private static void initializeHorizontalPoints() {
        for (int row = 0; row < Constants.NUM_ROWS; row++) {
            HorizontalLines.add(new ArrayList<>());
        }

        for (int row = 0; row < Constants.NUM_ROWS; row++) {
            for (int col = 0; col < Constants.NUM_COLS; col++) {
                HorizontalLines.get(row).add(new Point2D(col, row));
            }
        }
    }

    /**
     * Creates a list of all possible lists of four points in a vertical row.
     */
    private static void initializeVerticalPoints() {
        for (int col = 0; col < Constants.NUM_COLS; col++) {
            VerticalLines.add(new ArrayList<>());
        }

        for (int col = 0; col < Constants.NUM_COLS; col++) {
            for (int row = 0; row < Constants.NUM_ROWS; row++) {
                VerticalLines.get(col).add(new Point2D(col, row));
            }
        }
    }

    /**
     * Creates a list of all possible lists of four points in an upward sloping diagonal row.
     */
    private static void initializeUpwardDiagonalPoints() {
        for (int x = 0; x < Constants.NUM_COLS + Constants.NUM_ROWS - 7; x++) {
            UpwardDiagonalLines.add(new ArrayList<>());
        }

        UpwardDiagonalLines.add(createUpwardDiagonal(0, 2));
        UpwardDiagonalLines.add(createUpwardDiagonal(0, 1));
        UpwardDiagonalLines.add(createUpwardDiagonal(0, 0));
        UpwardDiagonalLines.add(createUpwardDiagonal(1, 0));
        UpwardDiagonalLines.add(createUpwardDiagonal(2, 0));
        UpwardDiagonalLines.add(createUpwardDiagonal(3, 0));
    }

    /**
     * Creates a list of all possible lists of four points in a downward sloping diagonal row.
     */
    private static void initializeDownwardDiagonalPoints() {
        for (int x = 0; x < Constants.NUM_COLS + Constants.NUM_ROWS - 7; x++) {
            DownwardDiagonalLines.add(new ArrayList<>());
        }

        DownwardDiagonalLines.add(createDownwardDiagonal(0, 3));
        DownwardDiagonalLines.add(createDownwardDiagonal(0, 4));
        DownwardDiagonalLines.add(createDownwardDiagonal(0, 5));
        DownwardDiagonalLines.add(createDownwardDiagonal(1, 5));
        DownwardDiagonalLines.add(createDownwardDiagonal(2, 5));
        DownwardDiagonalLines.add(createDownwardDiagonal(3, 5));
    }

    /**
     * Creates an upward sloping diagonal line of four points starting at the given location.
     *
     * @param col the column value to start the diagonal line.
     * @param row the row value to start the diagonal line.
     * @return a list of four points in an upward diagonal line.
     */
    private static List<Point2D> createUpwardDiagonal(int col, int row) {
        List<Point2D> diagonal = new ArrayList<>();
        int r = row;
        for (int c = col; c < Constants.NUM_COLS && r < Constants.NUM_ROWS; c++) {
            diagonal.add(new Point2D(c, r));
            r++;
        }
        return diagonal;
    }

    /**
     * Creates a downward sloping diagonal line of four points starting at the given location.
     *
     * @param col the column value to start the diagonal line.
     * @param row the row value to start the diagonal line.
     * @return a list of four points in a downward diagonal line.
     */
    private static List<Point2D> createDownwardDiagonal(int col, int row) {
        List<Point2D> diagonal = new ArrayList<>();
        int r = row;
        for (int c = col; c < Constants.NUM_COLS && r >= 0; c++) {
            diagonal.add(new Point2D(c, r));
            r--;
        }
        return diagonal;
    }
}
