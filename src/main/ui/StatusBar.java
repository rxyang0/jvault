package ui;

import javafx.scene.control.TextField;

// Displays status information
public class StatusBar extends TextField {

    public StatusBar() {
        this.setEditable(false);
        this.setMouseTransparent(true);
        showStatus("No Vault Loaded");
    }

    // EFFECTS: displays text in black
    public void showStatus(String text) {
        this.setStyle("-fx-text-fill: black;");
        this.setText(text);
    }

    // EFFECTS: displays text in red
    public void showError(String text) {
        this.setStyle("-fx-text-fill: red;");
        this.setText(text);
    }

}
