package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// Main JavaFX application
public class FxApp extends Application {

    private static final String APP_TITLE = "JVault";

    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle(APP_TITLE);
        mainStage.setResizable(false);

        GridPane mainPane = new GridPane();

        Scene mainScene = new Scene(mainPane, 600, 400);
        mainStage.setScene(mainScene);
        mainStage.show();
    }

}
