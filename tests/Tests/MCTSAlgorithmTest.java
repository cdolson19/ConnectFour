package Tests;

import Application.Model.Board;
import Application.Constants;
import Application.Model.GameState;
import Application.Model.MonteCarloTreeSearch.MCTSAlgorithm;
import Application.Model.MonteCarloTreeSearch.MCTSTreeNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests the MCTS Algorithm class.
 */
public class MCTSAlgorithmTest {

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
     * Tests if the getColumnToMoveInto method finds the correct column to place a token to move to the desired state.
     */
    @Test
    public void testGetColumnToMoveInto() {
        setUpMCTSTreeNode();
        MCTSTreeNode parentNode = new MCTSTreeNode(new GameState(new Board()));
        int expectedColumn = 3;
        int actualColumn = MCTSAlgorithm.getColumnToMoveInto(parentNode.getGameState().getBoard(), node.getGameState().getBoard());
        Assert.assertSame("The column to move into is not the expected result.", expectedColumn, actualColumn);
    }

    /**
     * Test if the selectPromisingNode method returns the successor node with the highest UCT value.
     */
    @Test
    public void testSelectPromisingNode() {
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

        Assert.assertTrue("The selectPromisingNode method did not return the successor node with the best UCT value.",
                MCTSAlgorithm.selectPromisingNode(node).equals(node1));
    }

    /**
     * Tests if the expandNode method expands the root node by creating successor nodes with all possible next board states
     * using all possible actions.
     */
    @Test
    public void testExpandNode() {
        setUpMCTSTreeNode();

        List<Board> expectedSuccessorBoards = new ArrayList<>();
        Board board1 = new Board(board);
        board1.performMove(Constants.COMP_MOVE, 0, Constants.NUM_ROWS-1);
        expectedSuccessorBoards.add(board1);

        Board board2 = new Board(board);
        board2.performMove(Constants.COMP_MOVE, 1, Constants.NUM_ROWS-1);
        expectedSuccessorBoards.add(board2);

        Board board3 = new Board(board);
        board3.performMove(Constants.COMP_MOVE, 2, Constants.NUM_ROWS-1);
        expectedSuccessorBoards.add(board3);

        Board board4 = new Board(board);
        board4.performMove(Constants.COMP_MOVE, 3, Constants.NUM_ROWS-2);
        expectedSuccessorBoards.add(board4);

        Board board5 = new Board(board);
        board5.performMove(Constants.COMP_MOVE, 4, Constants.NUM_ROWS-1);
        expectedSuccessorBoards.add(board5);

        Board board6 = new Board(board);
        board6.performMove(Constants.COMP_MOVE, 5, Constants.NUM_ROWS-1);
        expectedSuccessorBoards.add(board6);

        Board board7 = new Board(board);
        board7.performMove(Constants.COMP_MOVE, 6, Constants.NUM_ROWS-1);
        expectedSuccessorBoards.add(board7);

        MCTSAlgorithm.expandNode(node);

        List<Board> successorBoards = new ArrayList<>();
        for (int i = 0; i < node.getSuccessorStates().size(); i++) {
            successorBoards.add(node.getSuccessorStates().get(i).getGameState().getBoard());
        }

        Assert.assertSame("The list of successor states is not the expected length.",
                expectedSuccessorBoards.size(), successorBoards.size());

        for (Board expectedBoard : expectedSuccessorBoards) {
            Assert.assertTrue("The expanded successor states do no equal the expected successor states.",
                    successorBoards.contains(expectedBoard));
        }
    }

    /**
     * Tests that the backPropagation traverses through the node's parents and correctly updates the user wins,
     * comp wins, draws, and node visit values.
     */
    @Test
    public void testBackPropagation() {
        setUpMCTSTreeNode();

        Board board1 = new Board(board);
        board1.performMove(Constants.COMP_MOVE, 0, Constants.NUM_ROWS-1);
        MCTSTreeNode node1 = new MCTSTreeNode(new GameState(board1));
        node1.setParentNode(node);
        node.getSuccessorStates().add(node1);

        Board board2 = new Board(board1);
        board2.performMove(Constants.USER_MOVE, 1, Constants.NUM_ROWS-1);
        MCTSTreeNode node2 = new MCTSTreeNode(new GameState(board2));
        node2.setParentNode(node1);
        node1.getSuccessorStates().add(node2);

        MCTSAlgorithm.backPropagation(node2, Constants.USER_WIN);
        assertTrue("The number of user wins in the leaf node is not equal to the expected value.",
                1 == node2.getUserWins());
        assertTrue("The number of user wins in the middle node is not equal to the expected value.",
                1 == node1.getUserWins());
        assertTrue("The number of user wins in the root node is not equal to the expected value.",
                1 == node.getUserWins());

        MCTSAlgorithm.backPropagation(node2, Constants.COMP_WIN);
        assertTrue("The number of comp wins in the leaf node is not equal to the expected value.",
                1 == node2.getCompWins());
        assertTrue("The number of comp wins in the middle node is not equal to the expected value.",
                1 == node1.getCompWins());
        assertTrue("The number of comp wins in the root node is not equal to the expected value.",
                1 == node.getCompWins());

        MCTSAlgorithm.backPropagation(node2, Constants.DRAW_SCORE);
        assertTrue("The number of draws in the leaf node is not equal to the expected value.",
                1 == node2.getDraws());
        assertTrue("The number of draws in the middle node is not equal to the expected value.",
                1 == node1.getDraws());
        assertTrue("The number of draws in the root node is not equal to the expected value.",
                1 == node.getDraws());

        assertTrue("The number of visits in the leaf node is not equal to the expected value.",
                3 == node2.getVisitCount());
        assertTrue("The number of visits in the middle node is not equal to the expected value.",
                3 == node1.getVisitCount());
        assertTrue("The number of visits in the root node is not equal to the expected value.",
                3 == node.getVisitCount());
    }

    /**
     * Tests that the simulateRandomPlayout method randomly finishes a game by verifying that the game status that is
     * returned in not equal to the IN_PROGRESS constant value.
     */
    @Test
    public void testSimulateRandomPlayout() {
        setUpMCTSTreeNode();
        double resultingGameStatus = MCTSAlgorithm.simulateRandomPlayout(node);
        Assert.assertNotSame("The simulateRandomPlayout method should not return a game status of still in progress.",
                Constants.IN_PROGRESS, resultingGameStatus);
    }
}