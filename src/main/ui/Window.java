package ui;

import javafx.scene.layout.BorderPane;

// Main application pane
public class Window extends BorderPane {

    private static Window window;

    protected MenuBar menuBar;
    protected VaultExplorer explorer;
    protected StatusBar statusBar;

    // EFFECTS: prevent direct construction; add elements to window
    private Window() {}

    // MODIFIES: this
    // EFFECTS: singleton lazy initialization
    public static Window getInstance() {
        if (window == null) {
            window = new Window();
            window.addNodes();
        }
        return window;
    }

    // MODIFIES: this
    // EFFECTS: adds elements to the window
    private void addNodes() {
        menuBar = new MenuBar();
        window.setTop(menuBar);

        explorer = new VaultExplorer();
        window.setCenter(explorer);

        statusBar = new StatusBar();
        window.setBottom(statusBar);
    }

}
