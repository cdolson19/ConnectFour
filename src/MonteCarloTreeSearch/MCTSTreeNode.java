package MonteCarloTreeSearch;

import Application.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTSTreeNode {
    private GameState gameState;
    private MCTSTreeNode parentNode;
    private List<MCTSTreeNode> successorStates;

    public MCTSTreeNode(GameState state) {
        successorStates = new ArrayList<>();
        this.gameState = new GameState(state);
    }

    public MCTSTreeNode(MCTSTreeNode treeNode) {
        this.gameState = new GameState(treeNode.getGameState());
        this.parentNode = treeNode.getParentNode();
        this.successorStates = treeNode.getSuccessorStates();
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<MCTSTreeNode> getSuccessorStates() {
        return successorStates;
    }

    public MCTSTreeNode getRandomSuccessor() {
        Random random = new Random();
        int childIndex = random.nextInt(successorStates.size());
        return successorStates.get(childIndex);
    }

    public MCTSTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(MCTSTreeNode parent) {
        this.parentNode = parent;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public MCTSTreeNode getSuccessorWithMaxScore() {
        // DEBUG
        //System.out.println("Max Successor Value: " + UCT.findBestNodeWithUCT(this));
        return UCT.findBestNodeWithUCT(this);
    }

}
