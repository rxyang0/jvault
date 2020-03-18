package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Main JavaFX application
public class FxApp extends Application {

    private static final String APP_TITLE = "JVault";

    private Stage stage;
    private BorderPane window;
    private StatusBar statusBar;

    // MODIFIES: this
    // EFFECTS: initializes application UI
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setTitle(APP_TITLE);
        this.stage.setResizable(false);

        this.window = new BorderPane();
        this.addNodes();

        Scene scene = new Scene(window, 600, 400);
        this.stage.setScene(scene);
        this.stage.show();
    }

    // MODIFIES: this
    // EFFECTS: adds elements to the window
    private void addNodes() {
        MenuBar menuBar = new MenuBar(this);
        window.setTop(menuBar);

        VaultExplorer explorer = new VaultExplorer();
        window.setCenter(explorer);

        statusBar = new StatusBar();
        window.setBottom(statusBar);
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

}
