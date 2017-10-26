package MonteCarloTreeSearch;

import Application.Constants;
import Application.Disc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Application.Constants.NUM_COLS;

public class MCTSTreeNode {
    private int timesVisited;
    private double averageUserWins;
    private double userWins;
    private double compWins;
    private Disc[][] gameState;
    private MCTSTreeNode parentState;
    private List<MCTSTreeNode> successorStates;

    public MCTSTreeNode(Disc[][] state) {
        timesVisited = 0;
        averageUserWins = 0;
        successorStates = new ArrayList<>();
        gameState = state;
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



    public boolean rollout() {

        //while()
        return true;
    }
}
