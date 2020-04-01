package ui;

import javafx.scene.control.TextField;

// Displays status information
public class StatusBar extends TextField {

    public enum Error { IO, CRYPTO, DEFAULT }

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

    // EFFECTS: displays text in red
    public void showError(Error err, String operation, Exception e) {
        this.setStyle("-fx-text-fill: red;");
        switch (err) {
            case IO:
                this.setText("IO error when " + operation + ": " + e.getMessage());
                break;
            case CRYPTO:
                this.setText("Crypto error when " + operation + " (incorrect password?): " + e.getMessage());
                break;
            default:
                this.setText("Error when " + operation + ": " + e.getMessage());
        }
    }

}
