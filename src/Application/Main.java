package Application;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Main extends Application {

    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 7;
    private static final int TILE_SIZE = 80;

    private boolean playable = true;
    private boolean redMove = true;
    private Disc[][] grid = new Disc[NUM_COLS][NUM_ROWS];

    private Pane discRoot = new Pane();
    private Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the Application
     * @param primaryStage a Stage provided upon start
     * @throws Exception thrown if error during setup
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    /**
     * Creates the grid and column
     * @return the Pane that contains the grid and columns
     */
    private Parent createContent() {

        //Pane root = new Pane();
        root.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());

        return root;
    }

    /**
     * Makes the game's grid
     * @return a Shape representing the game board.
     */
    private Shape makeGrid() {
        // Create the rectangle which holds the game
        Shape shape = new Rectangle((NUM_COLS+1)*TILE_SIZE, (NUM_ROWS+1)*TILE_SIZE);
        // Create the circles that will
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Circle circle = new Circle(TILE_SIZE/2);
                circle.setCenterX(TILE_SIZE/2);
                circle.setCenterY(TILE_SIZE/2);
                circle.setTranslateX(col * (TILE_SIZE+5) + TILE_SIZE/4);
                circle.setTranslateY(row * (TILE_SIZE+5) + TILE_SIZE/4);

                shape = shape.subtract(shape, circle);
            }
        }
        // Create light
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        // Use light to create lighting
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        // Color the shape and apply lighting
        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);

        return shape;
    }

    /**
     * Makes the columns for the game
     * @return a list of rectangles, representing the columns
     */
    private List<Rectangle> makeColumns() {
        List<Rectangle> columns = new ArrayList<>();

        for (int x = 0; x < NUM_COLS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (NUM_ROWS+1)*TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE+5) + TILE_SIZE/4);
            rect.setFill(Color.TRANSPARENT);
            // Tint the column yellow when the user drags their mouse over it
            rect.setOnMouseEntered(event -> rect.setFill((Color.rgb(200,200,50,0.5))));
            // Return the column back to transparent when the mouse leaves it
            rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));
            // Attempt to place a disc into the column when the user clicks in the column
            final int column = x;
            rect.setOnMouseClicked(event -> placeDisc(new Disc(redMove),column));

            columns.add(rect);
        }
        return columns;
    }

    /**
     * Places the disc in the next available row in a given column.
     * @param disc the disc to place.
     * @param column the column to attempt to place the disc into.
     */
    private void placeDisc(Disc disc, int column) {
        // If computer's turn
        if (!redMove) {
            // DEBUG
            System.out.println("Computer's Turn");
            // Find the lowest row to place the disc
            int row = getNextEmptyRow(column);

            // Place the disc
            grid[column][row] = disc;
            discRoot.getChildren().add(disc);
            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

            final int currentRow = row;

            // Display the animation of the disc dropping into position
            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.25), disc);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation.setOnFinished(event -> {
                // Check if the game has ended
                if (gameEnded(column, currentRow)) {
                    gameOver();
                    //playable = false;
                } else {
                    // Change moves and allow the user to play again
                    playable = true;
                    redMove = !redMove;
                }
            });
            animation.play();
        } else {
            if (!playable) {
                return;
            }
            // DEBUG
            System.out.println("User's Turn");// Prevent the use from making another move
            playable = false;

            // Find the lowest row to place the disc
            int row = getNextEmptyRow(column);

            // Return if all rows were filled
            if (row < 0) {
                playable = true;
                return;
            }

            // Place the disc
            grid[column][row] = disc;
            discRoot.getChildren().add(disc);
            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

            final int currentRow = row;

            // Display the animation of the disc dropping into position
            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.25), disc);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation.setOnFinished(event -> {
                // Check if the game has ended
                if (gameEnded(column, currentRow)) {
                    gameOver();
                    //playable = false;
                } else {
                    // Change moves and allow the user to play again
//                    if (!redMove) {
//                        playable = true;
//                    }
                    redMove = !redMove;
                    computerTurn();
                }
            });
            animation.play();
        }
    }

    /**
     * Begins the MCTS algorithm to conduct the computer's next turn
     */
    private void computerTurn() {
        Random random = new Random();
        int column;
        do {
            column = random.nextInt(NUM_COLS);
            System.out.println("Computer Column: " + column);
        } while(!validColumn(column));
        // DEBUG
        System.out.println("Computer Column: " + column);

        placeDisc(new Disc(redMove),column);
    }

    /**
     * Denotes whether a move can be played into a given column by checking if the column is full.
     * @param column an int, the column number to check if it is full
     * @return true if a disc can be placed into the column, false if the column is full
     */
    private boolean validColumn(int column) {
        return !getDisc(column, 0).isPresent();
    }

    /**
     * Provides the next empty row in a particular column that a disc can be placed into.
     * @param column an int, the column number to find the next empty position
     * @return an int, the next empty row position
     */
    private int getNextEmptyRow(int column) {
        // Find the lowest row to place the disc
        int row = NUM_ROWS - 1;
        do {
            if (!getDisc(column, row).isPresent()) {
                break;
            }
            row--;
        } while (row >= 0);

        return row;
    }

    /**
     * Checks to see if the game ended by checking for potential game-winning scenarios created by entering a disc
     * in a given column and row.
     * @param column an int, the column position where the disc will be placed
     * @param row an int, the row position where the disc will be placed
     * @return true if the game is over, otherwise false.
     */
    private boolean gameEnded(int column, int row) {
        // Create a list of the vertical points in the given column containing the upper 3 points, the current point,
        // and the lower 3 points
        List<Point2D> vertical = new ArrayList<>();
        for (int r = row-3; r <= row+3; r++) {
            vertical.add(new Point2D(column, r));
        }

        // Create a list of the horizontal points in the given row containing the left 3 points, the current point,
        // and the right 3 points
        List<Point2D> horizontal = new ArrayList<>();
        for (int c = column-3; c <= column+3; c++) {
            horizontal.add(new Point2D(c, row));
        }

        // Create a list of the diagonal points containing the upper-left 3 points,
        // the current point, and the lower-right 3 points
        List<Point2D> diagonal1 = new ArrayList<>();
        int r = row - 3;
        for (int c = column-3; c <= column+3; c++) {
            diagonal1.add(new Point2D(c, r));
            r++;
        }

        // Create a list of the diagonal points containing the lower-left 3 points,
        // the current point, and the upper-right points
        List<Point2D> diagonal2 = new ArrayList<>();
        r = row + 3;
        for (int c = column-3; c <= column+3; c++) {
            diagonal2.add(new Point2D(c, r));
            r--;
        }
        // DEBUG
        //System.out.println("Vertical: " + checkRange(vertical));
        //System.out.println("Horizontal: " + checkRange(horizontal));
        //System.out.println("Top Left Diagonal: " + checkRange(diagonal1));
        //System.out.println("Bottom Left Diagonal: " + checkRange(diagonal2));
        return checkRange(vertical) || checkRange(horizontal) || checkRange(diagonal1) || checkRange(diagonal2);
    }

     /**
     * Given a list of points, check to see if four points in a row are the same color.
     * @param points a list of Point2D to check if four points in a row have the same color
     * @return true if four points in a row are the same color, otherwise false.
     */
    private boolean checkRange(List<Point2D> points) {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = getDisc(column, row).orElse((new Disc(!redMove)));
            if (disc.red == redMove) {
                chain++;
                // DEBUG
                System.out.println(chain);
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }
        return false;
    }

    /**
     * Notifies the user that the game is over
     */
    private void gameOver() {
        System.out.println("Winner: " + (redMove ? "Red" : "Yellow"));
        String winner = (redMove ? "Red" : "Yellow") + " won!";
        GameOverBox.display("Game Over", winner);
    }

    /**
     * Gets a disc from a particular column and row position in the grid.
     * @param column an int, the column number to obtain the disc
     * @param row an int, the row number to obtain the disc
     * @return the disc stored at the current position or empty
     */
    private Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= NUM_COLS || row < 0 || row >= NUM_ROWS) {
            return Optional.empty();
        }
        return Optional.ofNullable(grid[column][row]);
    }

    private static class Disc extends Circle {
        private final boolean red;

        Disc(boolean red) {
            super(TILE_SIZE/2, red ? Color.RED : Color.YELLOW);
            this.red = red;

            setCenterX(TILE_SIZE/2);
            setCenterY(TILE_SIZE/2);
        }
    }
}
