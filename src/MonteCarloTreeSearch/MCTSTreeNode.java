package MonteCarloTreeSearch;

import java.util.ArrayList;
import java.util.List;

public class MCTSTreeNode {
    private int timesVisited;
    private double averageUserWins;
    private double userWins;
    private double compWins;
    private MCTSTreeNode parentState;
    private List<MCTSTreeNode> successorStates;

    public MCTSTreeNode() {
        timesVisited = 0;
        averageUserWins = 0;
        successorStates = new ArrayList<>();
    }

    public int getTimesVisited() {
        return timesVisited;
    }

    public void visit() {
        timesVisited++;
    }

    public double getUCB1Value(int timesParentVisited) {
        return userWins/timesVisited + 2 * Math.sqrt(Math.log(timesParentVisited)/timesVisited);
    }

    public void generateSuccessors() {

    }
}
