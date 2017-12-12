package unitTests;

import application.Constants;
import model.environment.Board;
import model.environment.GameState;
import model.montecarlotreesearch.MCTSTreeNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the MCTSTreeNode class.
 */
public class MCTSTreeNodeTest {

    private Board board;
    private MCTSTreeNode node;

    /**
     * Initializes an MCTS Tree Node that contains a board where the user has placed a disc in the middle, bottom row
     * of the board.
     */
    private void setUpMCTSTreeNode() {
        board = new Board();
        board.performMove(Constants.USER_MOVE, 3, Constants.NUM_ROWS - 1);
        node = new MCTSTreeNode(new GameState(board));
    }

    /**
     * Tests if the get average state score returns the correct average.
     */
    @Test
    public void testGetAverageStateScore() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addDraw();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addUserWin();
        double expectedAverageStateScore = (1 * Constants.DRAW_SCORE + 3 * Constants.COMP_WIN + 1 * Constants.USER_WIN) / 5;
        Assert.assertEquals("The expected average state score does not equal the actual state score.",
                expectedAverageStateScore, node.getAverageStateScore(), 1e-5);

    }

    /**
     * Tests if the addUserWin method adds the correct number of user wins and if the add visits method
     * adds one to the node's visit count.
     */
    @Test
    public void testAddUserWin() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addDraw();
        int expectedUserWins = 1;
        int expectedVisits = 3;
        Assert.assertSame("The expected node user wins value is not equal to the actual user wins value.",
                expectedUserWins, node.getUserWins());
        Assert.assertSame("The expected node visit value is not equal to the actual visit value.",
                expectedVisits, node.getVisitCount());
    }

    /**
     * Tests if the addCompWin method adds the correct number of comp wins and if the add visits method
     * adds one to the node's visit count.
     */
    @Test
    public void testAddCompWin() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addDraw();
        int expectedCompWins = 1;
        int expectedVisits = 3;
        Assert.assertSame("The expected node comp wins value is not equal to the actual comp wins value.",
                expectedCompWins, node.getCompWins());
        Assert.assertSame("The expected node visit value is not equal to the actual visit value.",
                expectedVisits, node.getVisitCount());
    }

    /**
     * Tests if the addDraw method adds the correct number of draws and if the add visits method
     * adds one to the node's visit count.
     */
    @Test
    public void addDraw() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addCompWin();
        node.addVisit();
        node.addDraw();
        int expectedDraws = 1;
        int expectedVisits = 3;
        Assert.assertSame("The expected node draw value is not equal to the actual draw value.",
                expectedDraws, node.getDraws());
        Assert.assertSame("The expected node visit value is not equal to the actual visit value.",
                expectedVisits, node.getVisitCount());
    }

    /**
     * Test if the getSuccessorWithMaxScore returns the successor with the max average state score.
     */
    @Test
    public void getSuccessorWithMaxScore() {
        setUpMCTSTreeNode();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addUserWin();
        node.addVisit();
        node.addDraw();
        node.addVisit();
        node.addCompWin();

        MCTSTreeNode node1 = new MCTSTreeNode(new GameState(board));
        node1.addVisit();
        node1.addCompWin();
        node1.addVisit();
        node1.addCompWin();
        node.getSuccessorStates().add(node1);

        MCTSTreeNode node2 = new MCTSTreeNode(new GameState(board));
        node2.addVisit();
        node2.addUserWin();
        node2.addVisit();
        node2.addCompWin();
        node.getSuccessorStates().add(node2);

        MCTSTreeNode node3 = new MCTSTreeNode(new GameState(board));
        node3.addVisit();
        node3.addUserWin();
        node3.addVisit();
        node3.addDraw();
        node.getSuccessorStates().add(node3);

        Assert.assertTrue("The getSuccessorWithMaxScore did not return the node with the best average state score.",
                node.getSuccessorWithMaxScore().equals(node1));
    }
}