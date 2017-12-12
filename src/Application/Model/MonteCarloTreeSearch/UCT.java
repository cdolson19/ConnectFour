package Application.Model.MonteCarloTreeSearch;

import Application.Constants;

import java.util.Collections;
import java.util.Comparator;

/**
 * This class executes the UCT algorithm.
 */
public class UCT {

    /**
     * This class calculates the UCT value of the given node.
     *
     * @param parentVisits the number of times the parent has been visited.
     * @param node         the node to calculate the UCT value for.
     * @return Double.MaxValue if the node has not been visited, otherwise
     * the node's average state score + UCT-Exploration-Constant * Square root of
     * (ln(parentVisits)/nodeVisits)
     */
    public static double uctValue(int parentVisits, MCTSTreeNode node) {
        if (node.getVisitCount() == 0.0) {
            return Double.MAX_VALUE;
        }
        return (node.getAverageStateScore() + Constants.UCT_EXPLORATION_CONSTANT *
                Math.sqrt(Math.log(parentVisits) / node.getVisitCount()));
    }

    /**
     * Returns the MCTS tree node that is a successor of the given node with the max UCT value.
     *
     * @param node the node to find the best successor from.
     * @return the MCTS tree node that maximizes the UCT value.
     */
    public static MCTSTreeNode findBestNodeWithUCT(MCTSTreeNode node) {
        int parentVisit = node.getVisitCount();
        return Collections.max(
                node.getSuccessorStates(),
                Comparator.comparing(c -> uctValue(parentVisit, c)));
    }
}
