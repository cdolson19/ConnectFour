package MonteCarloTreeSearch;

import Application.Board;
import Application.GameState;

public class MCTSTree {
    private MCTSTreeNode root;

    public MCTSTree(GameState state) {
        root = new MCTSTreeNode(state);
    }

    public MCTSTree(Board board) {
        GameState gameState = new GameState(board);
        root = new MCTSTreeNode(gameState);
    }

    public MCTSTreeNode getRoot() {
        return root;
    }

    public void setRoot(MCTSTreeNode root) {
        this.root = root;
    }


}
