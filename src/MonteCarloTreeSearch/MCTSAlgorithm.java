package MonteCarloTreeSearch;

import Application.Board;
import Application.Constants;
import Application.GameState;

import java.util.List;

public class MCTSAlgorithm {
    static int level;
    static int opponent;

    public static int findNextMove(Board board, int playerNo) {
        // define an end time which will act as a terminating condition
        // DEBUG
        // System.out.println("findNextMove start");

        opponent = 1 - playerNo;
        MCTSTree mctsTree = new MCTSTree(new Board(board));
        MCTSTreeNode root = mctsTree.getRoot();
        //root.getGameState().setBoard(new Board(board));
        //root.getGameState().setPlayerNum(opponent);
        root.getGameState().setPlayerNum(playerNo);
        // DEBUG
        System.out.print(root.getGameState().getBoard().toString());

        int iteration = 0;
        while (iteration++ < 100) {
            MCTSTreeNode promisingNode = selectPromisingNode(root);
            // DEBUG
            // System.out.print("after selectPromisingNode");
            // System.out.print(root.getGameState().getBoard().toString());
            if (promisingNode.getGameState().getBoard().checkStatus()
                    == Constants.IN_PROGRESS) {
                expandNode(promisingNode);
            }
            // DEBUG
            // System.out.print("after expandNode");
            // System.out.print(root.getGameState().getBoard().toString());
            MCTSTreeNode nodeToExplore = promisingNode;
            // DEBUG
            // System.out.print("after re-assigning node");
            // System.out.print(root.getGameState().getBoard().toString());
            // If the node is not a leaf node, then get a random successor
            if (promisingNode.getSuccessorStates().size() > 0) {
                nodeToExplore = promisingNode.getRandomSuccessor();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            // DEBUG
            System.out.println("PlayoutResult: " + playoutResult);
            // System.out.print("after simulateRandomPlayout");
            // System.out.print(root.getGameState().getBoard().toString());
            backPropagation(nodeToExplore, playoutResult);
            // DEBUG
            // System.out.println(root.getGameState().getVisitCount());
            // System.out.print("after backPropagation");
            // System.out.print(root.getGameState().getBoard().toString());
        }


        MCTSTreeNode winnerNode = root.getSuccessorWithMaxScore();
        int column = getColumnToMoveInto(mctsTree.getRoot().getGameState().getBoard(), winnerNode.getGameState().getBoard());
        mctsTree.setRoot(winnerNode);
        // DEBUG
        // System.out.print("findNextMove ");
        // System.out.println("Col: " + column);
        return column;
    }

    private static int getColumnToMoveInto(Board currentBoard, Board nextBoard) {
        // DEBUG
        // System.out.println("getColumnToMoveInto");
        for (int row = 0; row < Constants.NUM_ROWS; row++) {
            for (int col = 0; col < Constants.NUM_COLS; col++) {
                // DEBUG
                //if (currentBoard.getDisc(col, row) != null) {
                //    System.out.println("Not Null Current Board Position (" + col + ", " + row + ")");
                //}
                //if (nextBoard.getDisc(col, row) != null) {
                //    System.out.println("Not Null Next Board Position (" + col + ", " + row + ")");
                //}
                if (currentBoard.getDisc(col, row) == null && nextBoard.getDisc(col, row) != null) {
                    return col;
                }
            }
        }
        return -1;
    }

    private static MCTSTreeNode selectPromisingNode(MCTSTreeNode rootNode) {
        MCTSTreeNode node = rootNode;
        // DEBUG
        // System.out.println("Node: " + node.getGameState().getBoard().toString());
        // System.out.println("Root: " + rootNode.getGameState().getBoard().toString());
        // Traverse down tree until a leaf is reached
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
    private static void expandNode(MCTSTreeNode node) {
        List<GameState> possibleStates = node.getGameState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            MCTSTreeNode newNode = new MCTSTreeNode(state);
            newNode.setParentNode(node);
            newNode.getGameState().setPlayerNum(node.getGameState().getOpponent());
            node.getSuccessorStates().add(newNode);
        });
    }

    private static void backPropagation(MCTSTreeNode nodeToExplore, int playerNo) {
        MCTSTreeNode tempNode = nodeToExplore;
        // DEBUG
        // System.out.println("Start of backPropagation");
        while (tempNode != null) {
            tempNode.getGameState().addVisit();
            // DEBUG
            // System.out.println("Visit Count: " + tempNode.getGameState().getVisitCount());
            if (tempNode.getGameState().getPlayerNum() == playerNo && playerNo == Constants.COMP_MOVE) {
                // DEBUG Test this COMP_WIN
                //tempNode.getGameState().addScore(Constants.COMP_WIN);
                tempNode.getGameState().addWin();
            }
            //else if (tempNode.getGameState().getPlayerNum() == playerNo && playerNo == Constants.USER_MOVE) {
            //    tempNode.getGameState().addScore(Constants.USER_WIN);
            //}
            tempNode = tempNode.getParentNode();
        }
    }


    // This needs debugging
    private static int simulateRandomPlayout(MCTSTreeNode node) {
        //MCTSTreeNode tempNode = new MCTSTreeNode(node);
        MCTSTreeNode tempNode = node;
        GameState tempState = tempNode.getGameState();
        int boardStatus = tempState.getBoard().checkStatus();
        if (boardStatus == Constants.USER_WIN) {
            //tempNode.getParentNode().getGameState().setWinScore(Constants.USER_WIN);
            tempNode.getParentNode().getGameState().setWinScore(Double.MIN_VALUE);
            return boardStatus;
        } else if (boardStatus == Constants.COMP_WIN) {
            //tempNode.getParentNode().getGameState().setWinScore(Constants.COMP_WIN);
            tempNode.getParentNode().getGameState().setWinScore(Double.MAX_VALUE);
            return boardStatus;
        } else if (boardStatus == Constants.DRAW_SCORE) {
            //tempNode.getParentNode().getGameState().setWinScore(Constants.DRAW_SCORE);
            return boardStatus;
        }
        while (boardStatus == Constants.IN_PROGRESS) {
            tempState.togglePlayer();
            tempState.randomPlay();
            boardStatus = tempState.getBoard().checkStatus();
        }
        return boardStatus;
    }
}
