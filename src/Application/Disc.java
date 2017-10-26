package Application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static Application.Constants.TILE_SIZE;

public class Disc extends Circle {

    private final boolean userMove;

    Disc(boolean userMove) {
        super(TILE_SIZE/2, userMove ? Color.RED : Color.YELLOW);
        this.userMove = userMove;

        setCenterX(TILE_SIZE/2);
        setCenterY(TILE_SIZE/2);
    }

    public boolean getMove() {
        return userMove;
    }
}
