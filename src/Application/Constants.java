package Application;

public class Constants {

    public static final int NUM_ROWS = 6;
    public static final int NUM_COLS = 7;
    public static final int TILE_SIZE = 80;

    public static final int IN_PROGRESS = -1;
    //public static final int DRAW = 0;
    public static final int DRAW_SCORE = 0;
    public static final int USER_WIN = -10;
    public static final int COMP_WIN = 10;

    public static final int USER_MOVE = 0;
    public static final int COMP_MOVE = 1;

    //public static final int WIN_SCORE = 10;
    //public static final int LOSE_SCORE = -10;

    public static double UCT_EXPLORATION_CONSTANT = Math.sqrt(2);
}
