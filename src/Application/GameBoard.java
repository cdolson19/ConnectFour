package Application;

import javafx.scene.Parent;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static Application.Constants.NUM_COLS;
import static Application.Constants.NUM_ROWS;
import static Application.Constants.TILE_SIZE;

class GameBoard {

    private static Pane discRoot = new Pane();

    /**
     * Creates the grid and column
     * @return the Pane that contains the grid and columns
     */
    static Parent createGameBoardContent(Pane root) {
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
    private static Shape makeGrid() {
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
    private static List<Rectangle> makeColumns() {
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
           // rect.setOnMouseClicked(event -> GameBoardController.placeDisc(new Disc(userMove),column));

            rect.setOnMouseClicked(event -> GameBoardController.placeDisc(new Disc(true),column));
            columns.add(rect);
        }
        return columns;
    }

    public static void addDiscToRoot(Disc disc) {
        discRoot.getChildren().add(disc);
    }

}
