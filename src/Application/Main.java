package Application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the Application
     * @param primaryStage a Stage provided upon start
     * @throws Exception thrown if error during setup
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(new Scene(GameBoard.createGameBoardContent(root)));
        primaryStage.show();
    }
}
