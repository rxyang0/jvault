package ui;

import javafx.scene.control.TextField;

public class StatusBar extends TextField {

    public StatusBar() {
        this.setEditable(false);
        this.setMouseTransparent(true);
        this.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
    }

}
