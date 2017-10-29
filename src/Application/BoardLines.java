package Application;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class BoardLines {

    private static List<List<Point2D>> HorizontalLines = new ArrayList<>();
    private static List<List<Point2D>> VerticalLines = new ArrayList<>();
    private static List<List<Point2D>> UpwardDiagonalLines = new ArrayList<>();
    private static List<List<Point2D>> DownwardDiagonalLines = new ArrayList<>();

    public BoardLines() {
        initializeHorizontalPoints();
        initializeVerticalPoints();
        initializeUpwardDiagonalPoints();
        initializeDownwardDiagonalPoints();
    }

    public List<List<Point2D>> getHorizontalLines() {
        return HorizontalLines;
    }

    public List<List<Point2D>> getVerticalLines() {
        return VerticalLines;
    }

    public List<List<Point2D>> getUpwardDiagonalLines() {
        return UpwardDiagonalLines;
    }

    public List<List<Point2D>> getDownwardDiagonalLines() {
        return DownwardDiagonalLines;
    }

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

    private static List<Point2D> createUpwardDiagonal(int col, int row) {
        List<Point2D> diagonal = new ArrayList<>();
        int r = row;
        for (int c = col; c < Constants.NUM_COLS && r < Constants.NUM_ROWS; c++) {
            diagonal.add(new Point2D(c, r));
            r++;
        }
        return diagonal;
    }

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
