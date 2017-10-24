package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

class GameOverBox {

    static void display(String title, String message) {
        Stage window = new Stage();

        // Set the window details
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(250);

        // Set the winner label details
        Label label = new Label();
        label.setText(message);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setFont(Font.font(24));

        // Set the button details
        Button closeButton = new Button("End Game");
        closeButton.setOnAction(event -> window.close());

        // Use a VBox to store the label and button
        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Create a scene and set the window
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }
}
