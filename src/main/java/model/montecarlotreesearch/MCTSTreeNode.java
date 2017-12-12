package model.montecarlotreesearch;

import application.Constants;
import model.environment.GameState;

import java.util.*;

/**
 * This class represents a tree node in the MCTS algorithm.
 */
public class MCTSTreeNode {
    private GameState gameState;
    private MCTSTreeNode parentNode;
    private List<MCTSTreeNode> successorStates;
    private int visitCount;
    private int compWins;
    private int userWins;
    private int draws;

    /**
     * Constructor
     *
     * @param state the game state to store in the MCTS node.
     */
    public MCTSTreeNode(GameState state) {
        successorStates = new ArrayList<>();
        this.gameState = new GameState(state);
        visitCount = 0;
        compWins = 0;
        userWins = 0;
        draws = 0;
    }

    /**
     * Returns the game state.
     *
     * @return the game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Returns the MCTS Tree Node's children.
     *
     * @return the successors of the current MCTS tree node.
     */
    public List<MCTSTreeNode> getSuccessorStates() {
        return successorStates;
    }

    /**
     * Returns a random successor.
     *
     * @return a random child.
     */
    public MCTSTreeNode getRandomSuccessor() {
        Random random = new Random();
        int childIndex = random.nextInt(successorStates.size());
        return successorStates.get(childIndex);
    }

    /**
     * Returns the visit count of the MCTS node.
     *
     * @return the visit count.
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Returns the number of computer wins that have occurred through this node.
     *
     * @return the number of computer wins.
     */
    public int getCompWins() {
        return compWins;
    }

    /**
     * Returns the number of user wins that have occurred through this node.
     *
     * @return the number of user wins.
     */
    public int getUserWins() {
        return userWins;
    }

    /**
     * Returns the number of draws that have occurred through this node.
     *
     * @return the number of draws.
     */
    public int getDraws() {
        return draws;
    }

    /**
     * Returns the average score of the MCTS node.
     *
     * @return the average score of the MCTS node.
     */
    public double getAverageStateScore() {
        return (draws * Constants.DRAW_SCORE + userWins * Constants.USER_WIN + compWins * Constants.COMP_WIN) / visitCount;
    }

    /**
     * Returns the parent node.
     *
     * @return the parent of this node.
     */
    public MCTSTreeNode getParentNode() {
        return parentNode;
    }

    /**
     * Sets the parent of this MCTS node.
     *
     * @param parent the parent to set.
     */
    public void setParentNode(MCTSTreeNode parent) {
        this.parentNode = parent;
    }

    /**
     * Adds 1 to the number of visits to the node.
     */
    public void addVisit() {
        visitCount++;
    }

    /**
     * Adds 1 to the number of user wins through the node.
     */
    public void addUserWin() {
        userWins++;
    }

    /**
     * Adds 1 to the number of computer wins through the node.
     */
    public void addCompWin() {
        compWins++;
    }

    /**
     * Adds 1 to the number of draws through the node.
     */
    public void addDraw() {
        draws++;
    }

    /**
     * Returns the successor with the max average state score.
     *
     * @return the child with the max average state score.
     */
    public MCTSTreeNode getSuccessorWithMaxScore() {
        return Collections.max(
                this.getSuccessorStates(),
                Comparator.comparing(c -> c.getAverageStateScore()));
    }
}
