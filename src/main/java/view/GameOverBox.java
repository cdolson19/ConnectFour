package view;

import application.GameBoardController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class displays a window stating the game's outcome once the game is over.
 */
public class GameOverBox {

    /**
     * Creates an end-of-game window with the given title and message.
     *
     * @param title   the title to postMCTSDisplay.
     * @param message the message to postMCTSDisplay.
     */
    public static void display(String title, String message) {
        Stage window = new Stage();

        // Set the window details
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(250);
        window.setResizable(false);

        // Set the winner label details
        Label label = new Label();
        label.setText(message);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setFont(Font.font(24));

        // Set the play again button details
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(event -> {
            window.close();
            GameBoardController.restartGame();
        });

        // Set the exit application button details
        Button closeButton = new Button("Exit Application");
        closeButton.setOnAction(event -> Platform.exit());

        // Use a VBox to store the label and button
        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, playAgainButton, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Create a scene and set the window
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }
}
