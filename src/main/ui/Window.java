package ui;

import javafx.scene.layout.BorderPane;

// Main application pane
public class Window extends BorderPane {

    protected MenuBar menuBar;
    protected VaultExplorer explorer;
    protected StatusBar statusBar;

    // EFFECTS: adds elements to window
    public Window() {
        menuBar = new MenuBar();
        this.setTop(menuBar);

        explorer = new VaultExplorer();
        this.setCenter(explorer);

        statusBar = new StatusBar();
        this.setBottom(statusBar);
    }

}
