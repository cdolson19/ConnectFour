package Tests;

import Application.Model.Board;
import Application.Constants;
import Application.Model.GameState;
import Application.Model.MonteCarloTreeSearch.MCTSTreeNode;
import Application.Model.MonteCarloTreeSearch.UCT;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the UCT class.
 */
public class UCTTest {

    private Board board;
    private MCTSTreeNode node;

    /**
     * Creates a MCTS Tree Node that contains a game state containing a board where the user has made a move to the
     * middle, bottom location in the game.
     */
    private void setUpMCTSTreeNode() {
        board = new Board();
        board.performMove(Constants.USER_MOVE, 3, Constants.NUM_ROWS - 1);
        node = new MCTSTreeNode(new GameState(board));
    }

    /**
     * Tests if the UCT value for an unvisited node is Double.Max_Value.
     */
    @Test
    public void testUctValueForUnvisitedNode() {
        setUpMCTSTreeNode();
        double expectedUCTValue = Double.MAX_VALUE;
        Assert.assertTrue("An unvisited node should return Double.max.",
                expectedUCTValue == UCT.uctValue(10, node));
    }

    /**
     * Tests if the UCT value for a visited node matches the UCT algorithm.
     */
    @Test
    public void testUctValueForVisitedNode() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addDraw();

        int parentVisits = 10;
        double expectedAverageStateScore = (1 * Constants.COMP_WIN + 1 * Constants.USER_WIN + 1 * Constants.DRAW_SCORE) / 3.0;
        double expectedUCTValue = expectedAverageStateScore + Constants.UCT_EXPLORATION_CONSTANT * Math.sqrt(Math.log(parentVisits) / node.getVisitCount());

        Assert.assertTrue("The visited node did not return the correct UCT value.",
                expectedUCTValue == UCT.uctValue(parentVisits, node));
    }

    /**
     * Tests if the findBestNodeWithUCT returns the expected successor node of the given root node.
     */
    @Test
    public void findBestNodeWithUCT() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addDraw();
        node.addVisit();
        node.addCompWin();

        Board successorBoard1 = new Board(board);
        successorBoard1.performMove(Constants.COMP_MOVE, 0, Constants.NUM_ROWS - 1);
        MCTSTreeNode node1 = new MCTSTreeNode(new GameState(successorBoard1));
        node1.addVisit();
        node1.addCompWin();
        node.getSuccessorStates().add(node1);

        Board successorBoard2 = new Board(board);
        successorBoard2.performMove(Constants.COMP_MOVE, 1, Constants.NUM_ROWS - 1);
        MCTSTreeNode node2 = new MCTSTreeNode(new GameState(successorBoard2));
        node2.addVisit();
        node2.addUserWin();
        node.getSuccessorStates().add(node2);

        Board successorBoard3 = new Board(board);
        successorBoard3.performMove(Constants.COMP_MOVE, 2, Constants.NUM_ROWS - 1);
        MCTSTreeNode node3 = new MCTSTreeNode(new GameState(successorBoard3));
        node3.addVisit();
        node3.addUserWin();
        node3.addVisit();
        node3.addDraw();
        node.getSuccessorStates().add(node3);

        Assert.assertTrue("The findBestNodeWithUCT did not return the node with the best UCT value.",
                UCT.findBestNodeWithUCT(node).equals(node1));
    }
}