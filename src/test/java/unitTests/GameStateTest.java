package unitTests;

import application.Constants;
import model.environment.Board;
import model.environment.GameState;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class test the GameState class.
 */
public class GameStateTest {

    private Board board;
    private GameState gameState;

    /**
     * Sets up a game state with an empty board.
     *
     * @return a game state with an empty board.
     */
    private GameState setUpGameState() {
        board = new Board();
        gameState = new GameState(board);
        return gameState;
    }

    /**
     * Tests if GameState.getAllPossibleNextStates returns 5 possible next states for the next move when 2 of the 7 columns
     * are filled.
     */
    @Test
    public void testGetAllPossibleNextStates1() {
        setUpGameState();

        // Fill column 3
        for (int row = Constants.NUM_ROWS - 1; row >= 0; row--) {
            if (row % 2 == 0) {
                board.performMove(Constants.COMP_MOVE, 3, row);
            } else {
                board.performMove(Constants.USER_MOVE, 3, row);
            }
        }

        // Fill column 2
        for (int row = Constants.NUM_ROWS - 1; row >= 0; row--) {
            if (row % 2 == 0) {
                board.performMove(Constants.COMP_MOVE, 2, row);
            } else {
                board.performMove(Constants.USER_MOVE, 2, row);
            }
        }
        Assert.assertSame("getAllPossibleNextStates did not return the correct number of states",
                5, gameState.getAllPossibleNextStates().size());
    }

    /**
     * Tests if GameState.getAllPossibleNextStates returns 7 possible next states for the next move when 0 of the 7 columns
     * are filled.
     */
    @Test
    public void testGetAllPossibleNextStates2() {
        gameState = setUpGameState();

        Assert.assertSame("getAllPossibleNextStates did not return the correct number of states",
                7, gameState.getAllPossibleNextStates().size());
    }
}