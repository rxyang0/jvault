package ui;

import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

// Displays and handles many functions in menus
public class MenuBar extends javafx.scene.control.MenuBar {

    private Menu fileMenu;
    private Menu editMenu;
    private MenuItem closeVault;

    // EFFECTS: constructs menu bar
    public MenuBar() {
        addFileMenu();
        addEditMenu();
        addViewMenu();
    }

    // MODIFIES: this, FxApp.getWindow()
    // EFFECTS: creates file menu and adds it to menu bar
    private void addFileMenu() {
        fileMenu = new Menu("File");

        MenuItem createVault = new MenuItem("Create Vault");
        createVault.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
        createVault.setOnAction(e -> handleCreateVault());

        MenuItem openVault = new MenuItem("Open Vault");
        openVault.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
        openVault.setOnAction(e -> handleOpenVault());

        closeVault = new MenuItem("Close Vault");
        closeVault.setAccelerator(KeyCombination.keyCombination("SHORTCUT+L"));
        closeVault.setDisable(true);

        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(createVault, openVault, new SeparatorMenuItem(), closeVault,
                new SeparatorMenuItem(), exit);
        this.getMenus().add(fileMenu);
    }

    // MODIFIES: this, FxApp.getWindow()
    // EFFECTS: creates edit menu and adds it to menu bar
    private void addEditMenu() {
        editMenu = new Menu("Edit");

        MenuItem add = new MenuItem("Add File");
        add.setOnAction(e -> handleAddFile());

        MenuItem createFolder = new MenuItem("Create Folder");
        createFolder.setOnAction(e -> handleCreateDir());

        MenuItem save = new MenuItem("Save File");

        MenuItem rename = new MenuItem("Rename");

        MenuItem delete = new MenuItem("Delete");

        MenuItem selectAll = new MenuItem("Select All");

        editMenu.getItems().addAll(add, createFolder, new SeparatorMenuItem(), save, rename, delete,
                new SeparatorMenuItem(), selectAll);
        editMenu.getItems().forEach(x -> x.setDisable(true));
        this.getMenus().add(editMenu);
    }

    // MODIFIES: this, FxApp.getWindow()
    // EFFECTS: creates view menu and adds it to menu bar
    private void addViewMenu() {
        Menu viewMenu = new Menu("View");

        CheckMenuItem showStatusBar = new CheckMenuItem("Status Bar");
        showStatusBar.setSelected(true);
        showStatusBar.setOnAction(e -> FxApp.getWindow().statusBar.setVisible(showStatusBar.isSelected()));

        viewMenu.getItems().addAll(showStatusBar);
        this.getMenus().add(viewMenu);
    }

    // MODIFIES: FxApp.getWindow()
    // EFFECTS: prompts user for name, password, and destination directory
    private void handleCreateVault() {
        Optional<String> name = prompt("Set Name of New Vault");
        if (!name.isPresent()) {
            return;
        }
        Optional<String> pass = prompt("Set Password of \"" + name.get() + "\"");
        if (!pass.isPresent()) {
            return;
        }
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Destination Folder");
        File destination = chooser.showDialog(null);
        if (destination != null) {
            if (new File(destination, name.get()).exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Directory already exists!");
                alert.setContentText("A directory with the name \"" + name.get() + "\" already exists in \""
                        + destination.getAbsolutePath() + "\"");
                alert.show();
            } else {
                FxApp.getWindow().explorer.loadVault(name.get(), destination, pass.get());
            }
        }
    }

    // MODIFIES: FxApp.getWindow()
    // EFFECTS: prompts user for vault directory and password
    private void handleOpenVault() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Vault");
        File destination = chooser.showDialog(null);
        if (destination == null) {
            return;
        } else if (!new File(destination, destination.getName() + ".jvault").exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("That Does Not Appear To Be a Vault!");
            alert.setContentText("Missing \"" + destination.getName() + ".jvault" + "\" file in \""
                    + destination.getAbsolutePath() + "\"");
            alert.show();
            return;
        }
        Optional<String> pass = prompt("Password to \"" + destination.getName() + "\"");
        if (!pass.isPresent()) {
            return;
        }
        FxApp.getWindow().explorer.loadVault(null, destination, pass.get());
    }

    // MODIFIES: FxApp.getWindow()
    // EFFECTS: prompts user to select file to add and delegates to VaultExplorer
    private void handleAddFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File to Add");
        File file = chooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        FxApp.getWindow().explorer.addFile(file);
    }

    // MODIFIES: FxApp.getWindow()
    // EFFECTS: prompts user for new directory name and gets VaultExplorer to create it
    private void handleCreateDir() {
        Optional<String> name = prompt("Folder Name");
        if (!name.isPresent()) {
            return;
        }
        FxApp.getWindow().explorer.createDir(name.get());
    }

    // EFFECTS: prompts user and returns text input
    private Optional<String> prompt(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText("");
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue)
                -> dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(newValue.equals("")));
        return dialog.showAndWait();
    }

    // EFFECTS: enables or disables all menu items relevant to a loaded vault
    protected void setStateVaultMenuItems(boolean state) {
        closeVault.setDisable(!state);
        editMenu.getItems().forEach(x -> x.setDisable(!state));
    }

}
