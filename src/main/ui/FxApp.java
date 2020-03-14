package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// Main JavaFX application
public class FxApp extends Application {

    private static final String APP_TITLE = "JVault";

    private Stage stage;
    private GridPane window;

    // MODIFIES: this
    // EFFECTS: initializes application UI
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setTitle(APP_TITLE);
        this.stage.setResizable(false);

        this.window = new GridPane();
        this.addNodes();

        Scene scene = new Scene(window, 600, 400);
        this.stage.setScene(scene);
        this.stage.show();
    }

    // MODIFIES: this
    // EFFECTS: adds elements to the window
    private void addNodes() {
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        window.add(menuBar, 0, 0);
    }

}
