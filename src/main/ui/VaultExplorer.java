package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class VaultExplorer extends BorderPane {

    public VaultExplorer() {
        addAddressBar();
        addFileExplorer();
    }

    private void addAddressBar() {
        Pane addressBar = new Pane();

        Button back = new Button("←");
        addressBar.getChildren().add(back);

        this.setTop(addressBar);
    }

    private void addFileExplorer() {

    }

}
