package Application;

import Application.Model.Board;
import Application.Model.Disc;
import Application.Model.GameState;
import Application.View.GameBoard;
import Application.View.GameOverBox;
import Application.View.MCTSInfoBox;
import Application.Model.MonteCarloTreeSearch.MCTSAlgorithm;
import Application.Model.MonteCarloTreeSearch.MCTSTreeNode;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import static Application.Constants.TILE_SIZE;

/**
 * This class is a controller for the javafx view of the connect 4 board.
 */
public class GameBoardController {

    private static boolean playable = true;
    private static boolean userMove = true;
    private static Board board = new Board();
    private static GameState gameState = new GameState(board);
    private static MCTSTreeNode rootNode = new MCTSTreeNode(gameState);

    /**
     * Places the disc in the next available row in a given column.
     *
     * @param playerID the ID of the player or computer depending on who is making the move.
     * @param column   the column to attempt to place the disc into.
     */
    public static void placeDisc(int playerID, int column) {
        if (userMove && !playable) {
            return;
        }
        // Prevent the user from making another move
        playable = false;

        // Find the lowest row to place the disc
        int row = board.getNextEmptyRow(column);

        // Return if all rows were filled
        if (userMove && row < 0) {
            playable = true;
            return;
        }

        // Place the disc
        addDiscToBoard(playerID, column, row);
    }

    /**
     * Add a disc to the given location.
     *
     * @param playerID the player who made the move.
     * @param column   the column to place the disc.
     * @param row      the row to place the disc.
     */
    private static void addDiscToBoard(int playerID, int column, int row) {
        Disc disc = gameState.getBoard().performMove(playerID, column, row);
        // Find the successor node from the current node that represents the next move.
        MCTSTreeNode nextNode = null;
        for (MCTSTreeNode node : rootNode.getSuccessorStates()) {
            if (node.getGameState().getBoard().equals(gameState.getBoard())) {
                nextNode = node;
            }
        }
        if (nextNode == null) {
            nextNode = new MCTSTreeNode(gameState);
            nextNode.setParentNode(rootNode);
        }
        rootNode = nextNode;

        // Add the disc to the board.
        GameBoard.addDiscToRoot(disc);
        disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
        // Begin the animation to add the disc to the board.
        addDiscToBoardAnimation(row, disc);
    }

    /**
     * Executes animation of token falling down a column.
     *
     * @param row  the row to place the token.
     * @param disc the disc to place.
     */
    private static void addDiscToBoardAnimation(int row, Disc disc) {
        // Display the animation of the disc dropping into position
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.25), disc);
        animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        animation.setOnFinished(event -> {
            // Check if the game has ended
            if (!gameOver()) {
                if (userMove) {
                    userMove = !userMove;
                    showPreMCTSInformation();
                } else {
                    // Allow the user to play again
                    playable = true;
                    userMove = !userMove;
                }
            }
        });
        animation.play();
    }

    /**
     * Checks if the game is over and opens the game over postMCTSDisplay if it is.
     *
     * @return true if the game is over, otherwise false.
     */
    private static boolean gameOver() {
        double gameStatus = gameState.getBoard().checkStatus();
        if (gameStatus == Constants.DRAW_SCORE) {
            displayGameOverDisplay(true);
            return true;
        } else if (gameStatus == Constants.COMP_WIN) {
            displayGameOverDisplay(false);
            return true;
        } else if (gameStatus == Constants.USER_WIN) {
            displayGameOverDisplay(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Notifies the user that the game is over
     */
    private static void displayGameOverDisplay(boolean draw) {
        String winner;
        if (draw) {
            winner = "The Game Was A Draw!";
        } else {
            winner = (userMove ? "Red" : "Yellow") + " Won!";
        }
        GameOverBox.display("Game Over", winner);
        userMove = true;
    }

    /**
     * Begins the MCTS algorithm to find the computer's next turn
     * by displaying the information box containing details about the current state of the MCTS tree.
     */
    private static void showPreMCTSInformation() {
        MCTSInfoBox.preMCTSDisplay(rootNode);

    }

    /**
     * Executes the MCTS algorithm to find the computer's next turn
     * and displays the information box containing details about the results of the MCTS tree.
     */
    public static void computerTurn() {
        int col = MCTSAlgorithm.findNextMove(rootNode);
        MCTSInfoBox.postMCTSDisplay(rootNode, col);
    }

    /**
     * Clears the board and restarts the game.
     */
    public static void restartGame() {
        GameBoard.clearDiscs();
        board = new Board();
        gameState = new GameState(board);
        rootNode = new MCTSTreeNode(gameState);
        MCTSAlgorithm.setIterations(Constants.ITERATIONS);
        userMove = true;
        playable = true;
    }
}
