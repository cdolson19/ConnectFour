package Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    private Board board;
    private int playerNum;
    private int visitCount;
    private double winScore;

    public GameState(Board board) {
        this.board = board;
        visitCount = 0;
        winScore = 0;
    }

    public GameState(GameState gameState) {
        this.board = new Board(gameState.getBoard());
        this.playerNum = gameState.getPlayerNum();
        this.visitCount = gameState.getVisitCount();
        this.winScore = gameState.getWinScore();
    }

    public Board getBoard() {
        return board;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getWinScore() {
        return winScore;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int getOpponent() {
        return playerNum == Constants.USER_MOVE ? Constants.COMP_MOVE : Constants.USER_MOVE;
    }

    public void setBoard(Board gameboard) {
        this.board = gameboard;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    public void addVisit() {
        visitCount++;
    }

    public void addWin(){
        winScore++;
    }

    public void addScore(double score) {
        winScore += score;
    }

    public void togglePlayer() {
        if (playerNum == Constants.USER_MOVE) {
            playerNum = Constants.COMP_MOVE;
        } else {
            playerNum = Constants.USER_MOVE;
        }
    }

    public List<GameState> getAllPossibleStates() {
        // DEBUG
        // System.out.println("getAllPossibleStates");
        List<GameState> successorStates = new ArrayList<>();
        List<Integer> possibleColumns = board.findPossibleColumns();
        for (Integer column : possibleColumns) {
            int row = board.getNextEmptyRow(column);
            // DEBUG
            // System.out.println("Column: " + column + " Row: " + row);
            Board successorBoard = new Board(board);
            successorBoard.performMove(getOpponent(), column, row);
            successorStates.add(new GameState(successorBoard));
        }
        return successorStates;
    }

    public void randomPlay() {
        // DEBUG
        // System.out.println("randomPlay");
        List<Integer> possibleColumns = board.findPossibleColumns();
        Random random = new Random();
        int columnIndex, column, row;
        columnIndex = random.nextInt(possibleColumns.size());
        column = possibleColumns.get(columnIndex);
        // Get the row to place the next disc
        row = board.getNextEmptyRow(column);
        // DEBUG
        // System.out.println("Row: " + row);
        // Add the disc to the discBoard
        board.performMove(playerNum, column, row);
    }

}
