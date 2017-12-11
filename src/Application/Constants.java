package Application;

/**
 * This class contains constants used throughout the program.
 */
public class Constants {

    public static final int NUM_ROWS = 6;
    public static final int NUM_COLS = 7;
    public static final int TILE_SIZE = 80;

    public static final double IN_PROGRESS = -0.1;
    public static final double DRAW_SCORE = 0.2;
    public static final double USER_WIN = -1.0;
    public static final double COMP_WIN = 0.7;

    public static final int USER_MOVE = 0;
    public static final int COMP_MOVE = 1;

    public static final int ITERATIONS = 1500;

    public static double UCT_EXPLORATION_CONSTANT = 2;
}
