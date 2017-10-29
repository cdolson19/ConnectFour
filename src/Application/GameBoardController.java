package Application;

import MonteCarloTreeSearch.MCTSAlgorithm;
import MonteCarloTreeSearch.MCTSTreeNode;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.Random;

import static Application.Constants.NUM_COLS;
import static Application.Constants.TILE_SIZE;

class GameBoardController {

    private static boolean playable = true;
    private static boolean userMove = true;
    private static Board board = new Board();
    private static GameState gameState = new GameState(board);
    private static MCTSTreeNode rootNode = new MCTSTreeNode(gameState);

    /**
     * Places the disc in the next available row in a given column.
     *
     * @param playerID the ID of the player or computer depending on who is making the move.
     * @param column the column to attempt to place the disc into.
     */
    static void placeDisc(int playerID, int column) {
        // DEBUG
        // System.out.println("placeDisc");
        if (userMove && !playable) {
            // DEBUG
            System.out.println("It is the user's turn, but not a playable time");
            return;
        }
        // Prevent the user from making another move
        playable = false;

        // Find the lowest row to place the disc
        int row = board.getNextEmptyRow(column);
        // DEBUG
        // System.out.println("Col: " + column + " Row: " + row);

        // Return if all rows were filled
        if (userMove && row < 0) {
            playable = true;
            return;
        }

        // Place the disc
        addDiscToBoard(playerID, column, row);
    }

    private static void addDiscToBoard(int playerID, int column, int row) {
        // DEBUG
        // System.out.println("addDiscToBoard");
        Disc disc = gameState.getBoard().performMove(playerID, column, row);
        GameBoard.addDiscToRoot(disc);
        disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

        addDiscToBoardAnimation(row, disc);
    }

    private static void addDiscToBoardAnimation(int row, Disc disc) {
        // DEBUG
        // System.out.println("addDiscToBoardAnimation");
        // Display the animation of the disc dropping into position
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.25), disc);
        animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        animation.setOnFinished(event -> {
            // Check if the game has ended
            if (!gameOver()) {
                if (userMove) {
                    // DEBUG
                    // System.out.println("The user completed their turn");
                    userMove = !userMove;
                    computerTurn();
                } else {
                    // Allow the user to play again
                    // DEBUG
                    // System.out.println("The computer completed its turn");
                    playable = true;
                    userMove = !userMove;
                }
            }
        });
        animation.play();
    }


    private static boolean gameOver() {
        int gameStatus = gameState.getBoard().checkStatus();
        if(gameStatus == Constants.DRAW_SCORE) {
            displayGameOverDisplay(true);
            return true;
        } else if(gameStatus == Constants.COMP_WIN) {
            displayGameOverDisplay(false);
            return true;
        } else if(gameStatus == Constants.USER_WIN) {
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
            // DEBUG
            System.out.println("Draw");
            winner = "The Game Was A Draw!";
        } else {
            // DEBUG
            System.out.println("Winner: " + (userMove ? "Red" : "Yellow"));
            winner = (userMove ? "Red" : "Yellow") + " Won!";
        }
        GameOverBox.display("Game Over", winner);
    }


    /**
     * Begins the MCTS algorithm to conduct the computer's next turn
     */
    private static void computerTurn() {
        //Random random = new Random();
        //int column;
        //do {
        //    column = random.nextInt(NUM_COLS);
        //} while (!gameState.getBoard().validColumn(column));
        // DEBUG
        //System.out.println("computerTurn start");
        int col = MCTSAlgorithm.findNextMove(board, Constants.USER_MOVE);

        // DEBUG
        //System.out.println("computerTurn");
        //System.out.println("Computer Column: " + col);

        placeDisc(Constants.COMP_MOVE, col);
    }


}
