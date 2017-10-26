package Application;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Application.Constants.NUM_COLS;
import static Application.Constants.NUM_ROWS;
import static Application.Constants.TILE_SIZE;

class GameBoardController {

    private static boolean playable = true;
    private static boolean userMove = true;
    private static GameBoardState gameBoardState = new GameBoardState();

    /**
     * Places the disc in the next available row in a given column.
     *
     * @param disc   the disc to place.
     * @param column the column to attempt to place the disc into.
     */
    static void placeDisc(Disc disc, int column) {
        // DEBUG
        System.out.println("placeDisc");
        if (userMove && !playable) {
            // DEBUG
            System.out.println("It is the user's turn, but not a playable time");
            return;
        }
        // Prevent the user from making another move
        playable = false;

        // Find the lowest row to place the disc
        int row = GameBoardStateHelper.getNextEmptyRow(column, gameBoardState.getGrid());

        // Return if all rows were filled
        if (userMove && row < 0) {
            playable = true;
            return;
        }

        // Place the disc
        addDiscToBoard(column, row, disc);
    }

    private static void addDiscToBoard(int column, int row, Disc disc) {
        // DEBUG
        System.out.println("addDiscToBoard");
        gameBoardState.insertDisc(column, row, disc);
        GameBoard.addDiscToRoot(disc);
        disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

        addDiscToBoardAnimation(column, row, disc);
    }

    private static void addDiscToBoardAnimation(int column, int row, Disc disc) {
        // DEBUG
        System.out.println("addDiscToBoardAnimation");
        // Display the animation of the disc dropping into position
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.25), disc);
        animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        animation.setOnFinished(event -> {
            // Check if the game has ended
            if (!gameOver(column, row)) {
                if (userMove) {
                    // DEBUG
                    System.out.println("The user completed their turn");
                    userMove = !userMove;
                    computerTurn();
                } else {
                    // Allow the user to play again
                    // DEBUG
                    System.out.println("The computer completed its turn");
                    playable = true;
                    userMove = !userMove;
                }
        }});
        animation.play();
    }


    private static boolean gameOver(int column, int row) {
        if (gameWon(column, row, gameBoardState.getGrid(), userMove)) {
            displayGameOverDisplay(false);
            return true;
        } else if (gameDraw()) {
            displayGameOverDisplay(true);
            return true;
        }
        return false;
    }

    private static boolean gameDraw() {
        return GameBoardStateHelper.findPossibleColumns(gameBoardState.getGrid()).isEmpty();
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
        Random random = new Random();
        int column;
        do {
            column = random.nextInt(NUM_COLS);
        } while (!gameBoardState.validColumn(column));
        // DEBUG
        System.out.println("Computer Column: " + column);

        placeDisc(new Disc(userMove), column);
    }

    /**
     * Checks to see if the game ended by checking for potential game-winning scenarios created by entering a disc
     * in a given column and row.
     *
     * @param column an int, the column position where the disc will be placed
     * @param row    an int, the row position where the disc will be placed
     * @return true if the game is over, otherwise false.
     */
    public static boolean gameWon(int column, int row, Disc[][] board, boolean userMove) {
        // Create a list of the vertical points in the given column containing the upper 3 points, the current point,
        // and the lower 3 points
        List<Point2D> vertical = new ArrayList<>();
        for (int r = row - 3; r <= row + 3; r++) {
            if(r >= 0 && r < NUM_ROWS) {
                vertical.add(new Point2D(column, r));
            }
        }

        // Create a list of the horizontal points in the given row containing the left 3 points, the current point,
        // and the right 3 points
        List<Point2D> horizontal = new ArrayList<>();
        for (int c = column - 3; c <= column + 3; c++) {
            if(c >= 0 && c < NUM_COLS) {
                horizontal.add(new Point2D(c, row));
            }
        }

        // Create a list of the diagonal points containing the upper-left 3 points,
        // the current point, and the lower-right 3 points
        List<Point2D> diagonal1 = new ArrayList<>();
        int r = row - 3;
        for (int c = column - 3; c <= column + 3; c++) {
            if(c >= 0 && r >= 0 && c < NUM_COLS && r < NUM_ROWS) {
                diagonal1.add(new Point2D(c, r));
            }
            r++;
        }

        // Create a list of the diagonal points containing the lower-left 3 points,
        // the current point, and the upper-right points
        List<Point2D> diagonal2 = new ArrayList<>();
        r = row + 3;
        for (int c = column - 3; c <= column + 3; c++) {
            if(c >= 0 && r >= 0 && c < NUM_COLS && r < NUM_ROWS) {
                diagonal2.add(new Point2D(c, r));
            }
            r--;
        }
        // DEBUG
        //System.out.println("Vertical: " + checkRange(vertical));
        //System.out.println("Horizontal: " + checkRange(horizontal));
        //System.out.println("Top Left Diagonal: " + checkRange(diagonal1));
        //System.out.println("Bottom Left Diagonal: " + checkRange(diagonal2));
        return checkRange(board, vertical, userMove) || checkRange(board, horizontal, userMove) || checkRange(board, diagonal1, userMove) || checkRange(board, diagonal2, userMove);
    }

    /**
     * Given a list of points, check to see if four points in a row are the same color.
     *
     * @param points a list of Point2D to check if four points in a row have the same color
     * @return true if four points in a row are the same color, otherwise false.
     */
    private static boolean checkRange(Disc[][] board, List<Point2D> points, boolean userMove) {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = board[column][row];
            if (disc != null && disc.getMove() == userMove) {
                chain++;
                // DEBUG
                // System.out.println(chain);
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }
        return false;
    }
}
