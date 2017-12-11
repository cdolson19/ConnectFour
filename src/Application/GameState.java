package Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the current state of the game.
 * Containing a board and the player's turn.
 */
public class GameState {
    private Board board;
    private int playerNum;

    /**
     * Constructor that sets the game state's board to be the given board.
     *
     * @param board the board to set.
     */
    GameState(Board board) {
        this.board = board;
    }

    /**
     * Constructor that uses a game state to set this game state's board and player number.
     *
     * @param gameState the game state used to initialize the new game state.
     */
    public GameState(GameState gameState) {
        this.board = new Board(gameState.getBoard());
        this.playerNum = gameState.getPlayerNum();
    }

    /**
     * Returns the board.
     *
     * @return the board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the player number
     *
     * @return 0 for user move, 1 for computer move.
     */
    private int getPlayerNum() {
        return playerNum;
    }

    /**
     * Returns the opponent to the current player.
     *
     * @return the player who will have the next move.
     */
    public int getOpponent() {
        return playerNum == Constants.USER_MOVE ? Constants.COMP_MOVE : Constants.USER_MOVE;
    }

    /**
     * Sets the player number.
     *
     * @param playerNum 0 for user move, 1 for computer move.
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Alternates whose turn it is.
     */
    public void togglePlayer() {
        if (playerNum == Constants.USER_MOVE) {
            playerNum = Constants.COMP_MOVE;
        } else {
            playerNum = Constants.USER_MOVE;
        }
    }

    /**
     * Returns a list of game states containing all possible next moves.
     *
     * @return a list of game states containing all possible next moves.
     */
    public List<GameState> getAllPossibleStates() {
        List<GameState> successorStates = new ArrayList<>();
        List<Integer> possibleColumns = board.findPossibleActions();
        for (Integer column : possibleColumns) {
            int row = board.getNextEmptyRow(column);
            Board successorBoard = new Board(board);
            successorBoard.performMove(getOpponent(), column, row);
            successorStates.add(new GameState(successorBoard));
        }
        return successorStates;
    }

    /**
     * Executes a random move on the board.
     */
    public void randomPlay() {
        List<Integer> possibleColumns = board.findPossibleActions();
        Random random = new Random();
        int columnIndex, column, row;
        columnIndex = random.nextInt(possibleColumns.size());
        column = possibleColumns.get(columnIndex);
        // Get the row to place the next disc
        row = board.getNextEmptyRow(column);
        // Add the disc to the discBoard
        board.performMove(playerNum, column, row);
    }
}
