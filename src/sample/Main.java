package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 7;

    private boolean playable = true;
    private boolean turnRed = true;
    private Tile[][] board = new Tile[NUM_ROWS][NUM_COLS];
    private List<Combo> combos = new ArrayList<>();

    private Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Connect Four");
        //primaryStage.setMaxHeight(600);
        //primaryStage.setMinHeight(600);
        //primaryStage.setMaxWidth(600);
        //primaryStage.setMinWidth(600);
        //Scene scene = new Scene(root);
        //primaryStage.setScene(new Scene(root, 300, 275));
        //root.prefHeight(600);
        //root.prefWidth(600);

        //primaryStage.setScene(scene);
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private Parent createContent() {

        root.setPrefSize(750, 650);

        for (int i = 0; i < NUM_ROWS; i ++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 100);
                tile.setTranslateY(i * 100);

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
        }

        return root;
    }

    private class Tile extends StackPane {
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
                    if (!turnRed) {
                        return;
                    }
                    drawRed();
                    turnRed = false;
                    checkState();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (turnRed) {
                        return;
                    }
                    drawBlue();
                    turnRed = true;
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
    }
}
