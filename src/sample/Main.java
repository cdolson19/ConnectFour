package sample;

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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Application {

    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 7;
    private static final int TILE_SIZE = 80;

    private boolean playable = true;
    private boolean redMove = true;
    //private Tile[][] board = new Tile[NUM_ROWS][NUM_COLS];
    private Disc[][] grid = new Disc[NUM_COLS][NUM_ROWS];

    private Pane discRoot = new Pane();

    //private List<Combo> combos = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((NUM_COLS+1)*TILE_SIZE, (NUM_ROWS+1)*TILE_SIZE);

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

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);

        return shape;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> columns = new ArrayList<>();

        for (int x = 0; x < NUM_COLS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (NUM_ROWS+1)*TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE+5) + TILE_SIZE/4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(event -> rect.setFill((Color.rgb(200,200,50,0.5))));
            rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(event -> placeDisc(new Disc(redMove),column));

            columns.add(rect);
        }

        return columns;
    }

    private void placeDisc(Disc disc, int column) {
        if(!playable) {
            return;
        }
        
        int row = NUM_ROWS - 1;

        do {
            if (!getDisc(column, row).isPresent()) {
                break;
            }
            row--;
        } while (row >= 0);

        if (row < 0)
            return;

        grid[column][row] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (TILE_SIZE+5) + TILE_SIZE/4);

        final int currentRow = row;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.25), disc);
        animation.setToY(row * (TILE_SIZE+5) + TILE_SIZE/4);
        animation.setOnFinished(event -> {

            if (gameEnded(column, currentRow)) {
                gameOver();
                playable = false;
            }
            redMove = !redMove;

        });
        animation.play();
    }

    private boolean gameEnded(int column, int row) {
        List<Point2D> vertical = new ArrayList<>();
        for (int r = row-3; r <= row+3; r++) {
            vertical.add(new Point2D(column, r));
        }

        List<Point2D> horizontal = new ArrayList<>();
        for (int c = column-3; c <= column+3; c++) {
            horizontal.add(new Point2D(c, row));
        }

        List<Point2D> diagonal1 = new ArrayList<>();
        int r = row - 3;
        for (int c = column-3; c <= column+3; c++) {
            diagonal1.add(new Point2D(c, r));
            r++;
        }

        List<Point2D> diagonal2 = new ArrayList<>();
        r = row + 3;
        for (int c = column-3; c <= column+3; c++) {
            diagonal2.add(new Point2D(c, r));
            r--;
        }
        /*
        List<Point2D> horizontal = IntStream.rangeClosed(column-3, column+3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column-3, row-3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj((e -> topLeft.add(e, e)))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column-3, row+3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj((e -> botLeft.add(e, -e)))
                .collect(Collectors.toList());*/

        System.out.println("Vertical: " + checkRange(vertical));
        System.out.println("Horizontal: " + checkRange(horizontal));
        System.out.println("Top Left Diagonal: " + checkRange(diagonal1));
        System.out.println("Bottom Left Diagonal: " + checkRange(diagonal2));
        return checkRange(vertical) || checkRange(horizontal) || checkRange(diagonal1) || checkRange(diagonal2);
    }

    private boolean checkRange(List<Point2D> points) {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = getDisc(column, row).orElse((new Disc(!redMove)));
            if (disc.red == redMove) {
                chain++;
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

    private void gameOver() {
        System.out.println("Winner: " + (redMove ? "Red" : "Yellow"));
    }

    private Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= NUM_COLS || row < 0 || row >= NUM_ROWS) {
            return Optional.empty();
        }
        return Optional.ofNullable(grid[column][row]);
    }


    private Parent createContent() {

        //root.setPrefSize(750, 650);
        Pane root = new Pane();
        root.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());


       /* for (int i = 0; i < NUM_ROWS; i ++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * TILE_SIZE);
                tile.setTranslateY(i * TILE_SIZE);

                root.getChildren().add(tile);

                board[i][j] = tile;
            }
        }

        // Horizontal Combos
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS-3; col++) {
                combos.add(new Combo(board[row][col], board[row][col+1], board[row][col+2], board[row][col+3]));
            }
        }

        // Vertical Combos
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = 0; row < NUM_ROWS-3; row++) {
                combos.add(new Combo(board[row][col], board[row+1][col], board[row+2][col], board[row+3][col]));
            }
        }

        // Forward Diagonal Combos
        for (int col = 0; col < NUM_COLS-3; col++) {
            for (int row = 0; row < NUM_ROWS-3; row++) {
                combos.add(new Combo(board[row][col], board[row+1][col+1], board[row+2][col+2], board[row+3][col+3]));
            }
        }

        // Backward Diagonal Combos
        for (int col = NUM_COLS-1; col >= 3; col--) {
            for (int row = NUM_ROWS-1; row >= 3; row--) {
                combos.add(new Combo(board[row][col], board[row-1][col-1], board[row-2][col-2], board[row-3][col-3]));
            }
        }*/

        return root;
    }

   /* private class Tile extends StackPane {
        private Text text = new Text();
        private int row;
        private int column;

        public Tile() {
            Rectangle border = new Rectangle(100, 100);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(36));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (!playable) {
                    return;
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!redMove) {
                        return;
                    }
                    drawRed();
                    redMove = false;
                    checkState();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (redMove) {
                        return;
                    }
                    drawBlue();
                    redMove = true;
                    checkState();
                }
            });
        }

        public String getValue() {
            if (text.getFill().equals(Color.RED)) {
                return "RED";
            } else if (text.getFill().equals(Color.BLUE)) {
                return "BLUE";
            }
            else {
                return "";
            }
        }

        private void drawRed() {
            text.setText("O");
            text.setFill(Color.RED);
        }

        private void drawBlue() {
            text.setText("O");
            text.setFill(Color.BLUE);
        }

        public double getCenterX() {
            return getTranslateX() + 50;
        }

        public double getCenterY() {
            return getTranslateY() + 50;
        }
    }

    private void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
    }

    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStrokeWidth(10);
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[3].getCenterX());
        line.setEndY(combo.tiles[3].getCenterY());

        line.setFill(Color.YELLOW);

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[3].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[3].getCenterY())));
        timeline.play();
    }

    private class Combo {
        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty()) {
                return false;
            }

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue())
                    && tiles[0].getValue().equals(tiles[3].getValue());
        }
    }*/

    private static class Disc extends Circle {
        private final boolean red;

        public Disc(boolean red) {
            super(TILE_SIZE/2, red ? Color.RED : Color.YELLOW);
            this.red = red;

            setCenterX(TILE_SIZE/2);
            setCenterY(TILE_SIZE/2);
        }
    }
}
