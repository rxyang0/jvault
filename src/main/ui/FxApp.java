package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main JavaFX application
public class FxApp extends Application {

    private static final String APP_TITLE = "JVault";

    // MODIFIES: this
    // EFFECTS: initializes application UI
    @Override
    public void start(Stage stage) {
        stage.setTitle(APP_TITLE);
        stage.setResizable(false);

        Scene scene = new Scene(Window.getInstance(), 600, 400);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

}
