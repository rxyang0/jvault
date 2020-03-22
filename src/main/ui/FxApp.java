package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main JavaFX application
public class FxApp extends Application {

    private static final String APP_NAME = "JVault";

    private static FxApp app;
    private Stage stage;
    private static Window window;

    // MODIFIES: this
    // EFFECTS: initializes application UI
    @Override
    public void start(Stage stage) {
        app = this;
        this.stage = stage;
        setTitle(null);

        window = new Window();

        Scene scene = new Scene(window, 600, 400);
        stage.setResizable(false);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    // MODIFIES: this
    // EFFECTS: sets app title to APP_NAME and appends vault name if present
    protected void setTitle(String vaultName) {
        if (vaultName == null) {
            stage.setTitle(APP_NAME);
        } else {
            stage.setTitle(APP_NAME + " - " + vaultName);
        }
    }

    protected static FxApp getApp() {
        return app;
    }

    protected static Window getWindow() {
        return window;
    }

}
