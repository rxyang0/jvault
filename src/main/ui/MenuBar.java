package ui;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

public class MenuBar extends javafx.scene.control.MenuBar {

    // EFFECTS: constructs menu bar
    public MenuBar() {
        this.setStyle("-fx-padding: 0; -fx-spacing: 0; -fx-font-size: 12px;");
        this.addFileMenu();
        this.addEditMenu();
        this.addViewMenu();
    }

    // MODIFIES: this
    // EFFECTS: creates file menu and adds it to menu bar
    private void addFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem createVault = new MenuItem("Create Vault");
        createVault.setAccelerator(KeyCombination.keyCombination("SHORTCUT+C"));

        MenuItem openVault = new MenuItem("Open Vault");
        openVault.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));

        MenuItem lockVault = new MenuItem("Lock");
        lockVault.setAccelerator(KeyCombination.keyCombination("SHORTCUT+L"));
        lockVault.setDisable(true);

        MenuItem closeVault = new MenuItem("Close Vault");
        closeVault.setDisable(true);

        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(createVault, openVault, closeVault, new SeparatorMenuItem(), lockVault,
                new SeparatorMenuItem(), exit);
        this.getMenus().add(fileMenu);
    }

    // MODIFIES: this
    // EFFECTS: creates edit menu and adds it to menu bar
    private void addEditMenu() {
        Menu editMenu = new Menu("Edit");

        MenuItem add = new MenuItem("Add File");

        MenuItem save = new MenuItem("Save File");

        MenuItem rename = new MenuItem("Rename");

        MenuItem delete = new MenuItem("Delete");

        MenuItem selectAll = new MenuItem("Select All");

        editMenu.getItems().addAll(add, save, new SeparatorMenuItem(), rename, delete,
                new SeparatorMenuItem(), selectAll);
        editMenu.getItems().forEach(x -> x.setDisable(true));
        this.getMenus().add(editMenu);
    }

    // MODIFIES: this
    // EFFECTS: creates view menu and adds it to menu bar
    private void addViewMenu() {
        Menu viewMenu = new Menu("View");

        CheckMenuItem showStatusBar = new CheckMenuItem("Status Bar");
        showStatusBar.setSelected(true);

        viewMenu.getItems().addAll(showStatusBar);
        this.getMenus().add(viewMenu);
    }

}
