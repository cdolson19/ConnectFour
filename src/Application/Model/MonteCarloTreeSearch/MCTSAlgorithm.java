package Application.Model.MonteCarloTreeSearch;

import Application.Model.Board;
import Application.Constants;
import Application.Model.GameState;

import java.util.List;

/**
 * This class implements the selection, expansion, simulation, and backpropagation steps
 * of the Monte Carlo Tree Search Algorithm
 */
public class MCTSAlgorithm {
    private static int iterations = Constants.ITERATIONS;

    /**
     * Sets the number of iterations to perform the MCTS algorithm
     *
     * @param newIterations the new iteration value
     */
    public static void setIterations(int newIterations) {
        iterations = newIterations;
    }

    /**
     * Returns the number of iterations to perform the MCTS algorithm
     *
     * @return the number of iterations
     */
    public static int getIterations() {
        return iterations;
    }

    /**
     * Finds the next move for the computer. Starts the MCTS algorithm.
     *
     * @param root the MCTS Tree Node that contains the current board.
     * @return the column number where the computer should place a disc.
     */
    public static int findNextMove(MCTSTreeNode root) {
        root.getGameState().setPlayerNum(Constants.USER_MOVE);

        for (int iteration = 0; iteration < iterations; iteration++) {
            // Selection step - select the most promising node.
            MCTSTreeNode promisingNode = selectPromisingNode(root);
            // Expansion step - expand node if the game is not over
            if (promisingNode.getGameState().getBoard().checkStatus() == Constants.IN_PROGRESS) {
                expandNode(promisingNode);
            }
            MCTSTreeNode nodeToExplore;
            // If the node is not a leaf node, then get a random successor
            if (promisingNode.getSuccessorStates().size() == 0) {
                nodeToExplore = promisingNode;
            } else {
                nodeToExplore = promisingNode.getRandomSuccessor();
            }
            // Repeat a specified number of simulations
            for(int simulation = 0; simulation < Constants.SIMULATIONS; simulation++) {
                double playoutResult = simulateRandomPlayout(nodeToExplore);
                backPropagation(nodeToExplore, playoutResult);
            }
        }

        MCTSTreeNode winnerNode = root.getSuccessorWithMaxScore();
        return getColumnToMoveInto(root.getGameState().getBoard(), winnerNode.getGameState().getBoard());
    }

    /**
     * Updates the iteration value to decrease by the number of total moves in the game so far.
     *
     * @param totalMoves the value to decrease the number of iterations by.
     */
    public static void updateIterations(int totalMoves) {
        iterations -= totalMoves;
    }

    /**
     * Uses the current board and the desired next board to find the action that will lead to the next board.
     *
     * @param currentBoard the current representation of the board.
     * @param nextBoard    the desired next representation of the board.
     * @return the column number to place the token to get the next move.
     */
    public static int getColumnToMoveInto(Board currentBoard, Board nextBoard) {
        for (int row = 0; row < Constants.NUM_ROWS; row++) {
            for (int col = 0; col < Constants.NUM_COLS; col++) {
                if (currentBoard.getDisc(col, row) == null && nextBoard.getDisc(col, row) != null) {
                    return col;
                }
            }
        }
        return -1;
    }

    /**
     * Selects the most promising node to explore by traversing down the nodes selecting the max UCT values.
     *
     * @param rootNode the node to start searching from.
     * @return the MCTS tree node to explore.
     */
    public static MCTSTreeNode selectPromisingNode(MCTSTreeNode rootNode) {
        MCTSTreeNode node = rootNode;
        while (node.getSuccessorStates().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    /**
     * Generate all successor states.
     *
     * @param node the MCTSTreeNode to find all successors of.
     */
    public static void expandNode(MCTSTreeNode node) {
        List<GameState> possibleStates = node.getGameState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            MCTSTreeNode newNode = new MCTSTreeNode(state);
            newNode.setParentNode(node);
            newNode.getGameState().setPlayerNum(node.getGameState().getOpponent());
            node.getSuccessorStates().add(newNode);
        });
    }

    /**
     * Conducts back propagation through the parents of the given node to update the nodes
     * based on the simulation result.
     *
     * @param nodeToExplore the node that was explored.
     * @param playoutResult the simulation result.
     */
    public static void backPropagation(MCTSTreeNode nodeToExplore, double playoutResult) {
        MCTSTreeNode tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.addVisit();
            if (playoutResult == Constants.COMP_WIN) {
                tempNode.addCompWin();
            } else if (playoutResult == Constants.USER_WIN) {
                tempNode.addUserWin();
            } else {
                tempNode.addDraw();
            }
            tempNode = tempNode.getParentNode();
        }
    }

    /**
     * Simulates the completion of a connect 4 game and returns the result.
     *
     * @param node the node to start simulation from.
     * @return a constants representing the result of the game.
     */
    public static double simulateRandomPlayout(MCTSTreeNode node) {
        // Simulate until over
        GameState tempState = new GameState(node.getGameState());
        double boardStatus = tempState.getBoard().checkStatus();
        while (boardStatus == Constants.IN_PROGRESS) {
            tempState.togglePlayer();
            tempState.randomPlay();
            boardStatus = tempState.getBoard().checkStatus();
        }
        return boardStatus;
    }
}
