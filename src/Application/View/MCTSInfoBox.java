package Application.View;

import Application.Constants;
import Application.GameBoardController;
import Application.Model.MonteCarloTreeSearch.MCTSAlgorithm;
import Application.Model.MonteCarloTreeSearch.MCTSTreeNode;
import Application.Model.MonteCarloTreeSearch.UCT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class displays a window providing MCTS details before the computer completes its turn.
 */
public class MCTSInfoBox {

    /**
     * Generates a label with the given string to be the title.
     * @param title a string to be the title.
     * @return a label containing the string.
     */
    private static Label generateTitleLabel(String title) {
        Label label = new Label(title);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setFont(Font.font(20));
        label.setPadding(new Insets(5, 5, 0, 5));
        return label;
    }

    /**
     * Generates a VBox to display information about the successors of the given root.
     * @param root the MCTS Tree Node to display the information about its successors.
     * @return the VBox containing the information.
     */
    private static VBox generateSuccessorInformationBox(MCTSTreeNode root) {
        VBox actionLayout = new VBox(5);
        actionLayout.setPadding(new Insets(5, 20, 5, 20));
        actionLayout.setAlignment(Pos.CENTER_LEFT);

        // List the constants used in the MCTS algorithm.
        Label constants = new Label(String.format("Constants:\n" +
                        "User Win Reward: %.2f   Comp Win Reward: %.2f   " +
                        "Draw Reward: %.2f    UCT Exploration Constant %.2f",
                Constants.USER_WIN, Constants.COMP_WIN, Constants.DRAW_SCORE, Constants.UCT_EXPLORATION_CONSTANT));
        actionLayout.getChildren().addAll(constants, new Separator());

        // Display MCTS data for each action
        int successorNum = 1;
        if(root.getSuccessorStates().size() == 0) {
            Label nodeLabel = new Label("No actions have been explored from the current game state yet");
            actionLayout.getChildren().addAll(nodeLabel, new Separator());
        }
        for (MCTSTreeNode successor : root.getSuccessorStates()) {
            int column = MCTSAlgorithm.getColumnToMoveInto(root.getGameState().getBoard(), successor.getGameState().getBoard());
            Label nodeLabel = new Label();
            StringBuilder builder = new StringBuilder(String.format("Option %d    -    Action: Place disc in column %d\n", successorNum++, column + 1));
            builder.append(String.format("UCT Value: %5.5f            ", UCT.uctValue(root.getVisitCount(), successor)));
            builder.append(String.format("Simulations Through This State: %d\n", successor.getVisitCount()));
            builder.append(String.format("Average Score: %.3f  -  ", successor.getAverageStateScore()));
            builder.append(String.format("Comp Wins: %d    ", successor.getCompWins()));
            builder.append(String.format("User Wins: %d    ", successor.getUserWins()));
            builder.append(String.format("Draws: %d\n", successor.getDraws()));
            nodeLabel.setText(builder.toString());
            actionLayout.getChildren().add(nodeLabel);
            actionLayout.getChildren().add(new Separator());
        }
        return actionLayout;
    }

    /**
     * Creates a window that displays MCTS information after execution of the MCTS.
     *
     * @param root the MCTS Tree Node root that contains the current game state.
     * @param col  the column where the computer will make its move.
     */
    public static void postMCTSDisplay(MCTSTreeNode root, int col) {
        Stage window = new Stage();

        // Set the window details
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Monte Carlo Tree Search Results");

        // Set a title label
        Label label = generateTitleLabel("Monte Carlo Tree Search Results");

        // Create a vertical box to contain the information for each possible action.
        VBox actionLayout = generateSuccessorInformationBox(root);

        // Display number of visits through root node and the action to take.
        Label iterationsLabel = new Label(String.format("MCTS Iterations Conducted This Turn: %d    Simulations per Iteration: %d", MCTSAlgorithm.getIterations(), Constants.SIMULATIONS));
        Label visitLabel = new Label(String.format("Cumulative Visits Through Current State: %d", root.getVisitCount()));
        Label action = new Label(String.format("Computer will place disc in column %d", col + 1));
        actionLayout.getChildren().addAll(iterationsLabel, visitLabel, new Separator(), action);

        // Set the button details
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(event -> {
            window.close();
            MCTSAlgorithm.updateIterations(root.getGameState().getBoard().getTotalMoves());
            GameBoardController.placeDisc(Constants.COMP_MOVE, col);
        });
        closeButton.setMinWidth(50);

        // Use a VBox to store the label and button
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, actionLayout, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5, 5, 5, 5));

        // Create a scene and set the window
        Scene scene = new Scene(layout);
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    /**
     * Creates a window that displays MCTS information before execution of the MCTS.
     *
     * @param root the MCTS Tree Node root that contains the current game state.
     */
    public static void preMCTSDisplay(MCTSTreeNode root) {
        Stage window = new Stage();

        // Set the window details
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Before Monte Carlo Tree Search");

        // Set a title label
        Label label = generateTitleLabel("Monte Carlo Tree Information Before MCTS Execution");

        // Create a vertical box to contain the information for each possible action.
        VBox actionLayout = generateSuccessorInformationBox(root);

        // Display number of visits through root node.
        Label visitLabel = new Label(String.format("Cumulative Visits Through Current State: %d", root.getVisitCount()));
        actionLayout.getChildren().addAll(visitLabel, new Separator());

        // Set the button details
        Button closeButton = new Button("Execute MCTS");
        closeButton.setOnAction(event -> {
            window.close();
            GameBoardController.computerTurn();
        });
        closeButton.setMinWidth(50);

        // Use a VBox to store the label and button
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, actionLayout, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5, 5, 5, 5));

        // Create a scene and set the window
        Scene scene = new Scene(layout);
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }
}
