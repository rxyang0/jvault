package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main JavaFX application
public class FxApp extends Application {

    private static final String APP_TITLE = "JVault";
    private static Window window;

    // MODIFIES: this
    // EFFECTS: initializes application UI
    @Override
    public void start(Stage stage) {
        stage.setTitle(APP_TITLE);
        stage.setResizable(false);

        window = new Window();

        Scene scene = new Scene(window, 600, 400);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public static Window getWindow() {
        return window;
    }

}
