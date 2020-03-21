package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

// Displays navigation controls, address bar, and vault file explorer
public class VaultExplorer extends BorderPane {

    private TextField address;
    private ListView<String> list;

    // EFFECTS: adds all elements to pane
    public VaultExplorer() {
        addAddressBar();
        addFileExplorer();
    }

    // MODIFIES: this
    // EFFECTS: adds navigation controls and address bar to pane
    private void addAddressBar() {
        HBox addressBar = new HBox();
        addressBar.setPadding(new Insets(2, 15, 0, 10));

        Button back = new Button("←");
        addressBar.getChildren().add(back);

        address = new TextField("No Vault Loaded");
        address.setEditable(false);
        address.setMouseTransparent(true);
        HBox.setHgrow(address, Priority.ALWAYS);
        addressBar.getChildren().add(address);

        this.setTop(addressBar);
    }

    // MODIFIES: this
    // EFFECTS: adds file explorer to pane
    private void addFileExplorer() {
        list = new ListView<>();
        list.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                // Double click
            }
        });
        list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Select item
        });
        this.setCenter(list);
    }

}
