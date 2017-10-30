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

        opponent = 1 - playerNo;
        MCTSTree mctsTree = new MCTSTree(new Board(board));
        MCTSTreeNode root = mctsTree.getRoot();
        //root.getGameState().setBoard(new Board(board));
        //root.getGameState().setPlayerNum(opponent);
        root.getGameState().setPlayerNum(playerNo);
        // DEBUG
        System.out.print(root.getGameState().getBoard().toString());


        // Comp can win next move
        MCTSTreeNode tempNode = new MCTSTreeNode(new GameState(root.getGameState()));
        int col = compCanWin(tempNode);
        if(col != -1) {
            return col;
        }

        // Can user win next move
        tempNode = new MCTSTreeNode(new GameState(root.getGameState()));
        col = userCanWin(tempNode);
        if(col != -1) {
            return col;
        }

        int iteration = 0;
        while (iteration++ < 100) {
            MCTSTreeNode promisingNode = selectPromisingNode(root);
            if (promisingNode.getGameState().getBoard().checkStatus()
                    == Constants.IN_PROGRESS) {
                expandNode(promisingNode);
            }
            MCTSTreeNode nodeToExplore = promisingNode;
            // If the node is not a leaf node, then get a random successor
            if (promisingNode.getSuccessorStates().size() > 0) {
                nodeToExplore = promisingNode.getRandomSuccessor();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropagation(nodeToExplore, playoutResult);
        }


        MCTSTreeNode winnerNode = root.getSuccessorWithMaxScore();
        int column = getColumnToMoveInto(mctsTree.getRoot().getGameState().getBoard(), winnerNode.getGameState().getBoard());
        mctsTree.setRoot(winnerNode);
        return column;
    }

    private static int getColumnToMoveInto(Board currentBoard, Board nextBoard) {
        for (int row = 0; row < Constants.NUM_ROWS; row++) {
            for (int col = 0; col < Constants.NUM_COLS; col++) {
                // DEBUG
                // if (currentBoard.getDisc(col, row) != null) {
                //    System.out.println("Not Null Current Board Position (" + col + ", " + row + ")");
                //}
                //System.out.println(currentBoard.toString());
                //if (nextBoard.getDisc(col, row) != null) {
                //    System.out.println("Not Null Next Board Position (" + col + ", " + row + ")");
                //}
                //System.out.println(nextBoard.toString());
                if (currentBoard.getDisc(col, row) == null && nextBoard.getDisc(col, row) != null) {
                    return col;
                }
            }
        }
        return -1;
    }

    private static MCTSTreeNode selectPromisingNode(MCTSTreeNode rootNode) {
        //MCTSTreeNode node = new MCTSTreeNode(rootNode);
        MCTSTreeNode node = rootNode;
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
        while (tempNode != null) {
            tempNode.getGameState().addVisit();
            if (tempNode.getGameState().getPlayerNum() == playerNo && playerNo == Constants.COMP_MOVE) {
                tempNode.getGameState().addWin();
            }
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

    private static int compCanWin(MCTSTreeNode node){
        MCTSTreeNode nextMove = node;
        //GameState gameState = nextMove.getGameState();
        //gameState.setPlayerNum(Constants.USER_MOVE);
        //nextMove.setGameState(gameState);
        nextMove.getGameState().setPlayerNum(Constants.USER_MOVE);
        expandNode(nextMove);
        for (MCTSTreeNode successorNode : nextMove.getSuccessorStates()) {
            if(successorNode.getGameState().getBoard().checkStatus() == Constants.COMP_WIN) {
                int col = getColumnToMoveInto(node.getGameState().getBoard(), successorNode.getGameState().getBoard());
                // DEBUG
                System.out.println("Comp can win at col: " + col);
                return col;
            }
        }

        return -1;
    }

    private static int userCanWin(MCTSTreeNode node){
        MCTSTreeNode nextMove = node;
        //GameState gameState = nextMove.getGameState();
        //gameState.setPlayerNum(Constants.COMP_MOVE);
        //nextMove.setGameState(gameState);
        nextMove.getGameState().setPlayerNum(Constants.COMP_MOVE);
        expandNode(nextMove);

        for (MCTSTreeNode successorNode : nextMove.getSuccessorStates()) {
            if(successorNode.getGameState().getBoard().checkStatus() == Constants.USER_WIN) {
                int col = getColumnToMoveInto(node.getGameState().getBoard(), successorNode.getGameState().getBoard());
                System.out.println("User can win at col: " + col);
                return col;
            }
        }

        return -1;
    }
}
