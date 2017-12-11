package Application.View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static Application.Constants.TILE_SIZE;

/**
 * This class represents a token that can be placed into the board.
 */
public class Disc extends Circle {

    private final boolean userMove;

    /**
     * Constructor, creates a disc for the given player.
     *
     * @param userMove which player placed the disc. True for user, false for computer.
     */
    public Disc(boolean userMove) {
        super(TILE_SIZE / 2, userMove ? Color.RED : Color.YELLOW);
        this.userMove = userMove;

        setCenterX(TILE_SIZE / 2);
        setCenterY(TILE_SIZE / 2);
    }

    /**
     * Returns the move value.
     *
     * @return true for user, false for computer.
     */
    public boolean getMove() {
        return userMove;
    }
}
