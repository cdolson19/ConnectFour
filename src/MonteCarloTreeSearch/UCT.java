package MonteCarloTreeSearch;

import Application.Constants;

import java.util.Collections;
import java.util.Comparator;

public class UCT {

    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        // DEBUG
        System.out.print("UCTValue for total visit " + totalVisit + " winScore " + nodeWinScore + " nodeVisit " + nodeVisit + " : ");
        if (nodeVisit == 0.0) {
            // DEBUG
            System.out.println("max");
            return Double.MAX_VALUE;
        }
        // DEBUG
        System.out.println((nodeWinScore / (double) nodeVisit)
                + Constants.UCT_EXPLORATION_CONSTANT * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit));
        return (nodeWinScore / (double) nodeVisit)
                + Constants.UCT_EXPLORATION_CONSTANT * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static MCTSTreeNode findBestNodeWithUCT(MCTSTreeNode node) {
        int parentVisit = node.getGameState().getVisitCount();
        return Collections.max(
                node.getSuccessorStates(),
                Comparator.comparing(c -> uctValue(parentVisit,
                        c.getGameState().getWinScore(), c.getGameState().getVisitCount())));
    }
}
