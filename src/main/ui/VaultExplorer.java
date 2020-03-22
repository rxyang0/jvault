package ui;

import exceptions.CryptoException;
import filesystem.Vault;
import filesystem.VaultDirectory;
import filesystem.VaultEntry;
import filesystem.VaultFile;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

// Displays navigation controls, address bar, and vault file explorer
public class VaultExplorer extends BorderPane {

    private Vault vault;
    private Button back;
    private TextField address;
    private ListView<String> list;
    private VaultDirectory currentDir;

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

        back = new Button("←");
        back.setDisable(true);
        addressBar.getChildren().add(back);

        address = new TextField();
        address.setEditable(false);
        address.setMouseTransparent(true);
        address.setDisable(true);
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
                for (VaultEntry vaultEntry : currentDir.getEntries()) {
                    if (vaultEntry.getName().equals(list.getSelectionModel().selectedItemProperty().get())
                            && vaultEntry.getClass().equals(VaultDirectory.class)) {
                        updateList((VaultDirectory) vaultEntry);
                        break;
                    }
                }
            }
        });
        this.setCenter(list);
    }

    // MODIFIES: this, FxApp.getWindow()
    // EFFECTS: if name is null, loads existing vault at dir
    //          else, creates new vault with name in dir,
    //          then enables UI elements and populates list of files
    protected void loadVault(String name, File directory, String password) {
        try {
            if (name == null) {     // Load vault
                vault = new Vault(directory, password.toCharArray());
            } else {                // Create vault
                vault = new Vault(new File(directory, name), password.toCharArray());
            }
        } catch (IOException e) {
            FxApp.getWindow().statusBar.showError("IO error when loading vault: " + e.getMessage());
            return;
        } catch (CryptoException e) {
            FxApp.getWindow().statusBar.showError("Crypto error when loading vault (maybe incorrect password?): "
                    + e.getMessage());
            return;
        }
        updateList(vault.getRoot());
        enableVaultExplorerUI(true);
        FxApp.getWindow().menuBar.enableVaultMenuBarItems(true);
        FxApp.getApp().setTitle(vault.getVaultFolder().getName());
        FxApp.getWindow().statusBar.showStatus("Loaded vault \"" + vault.getVaultFolder().getName() + "\"");
    }

    // MODIFIES: this
    // EFFECTS: closes vault and clears status bar
    protected void closeVault() {
        try {
            vault.lock();
            vault = null;
            enableVaultExplorerUI(false);
            FxApp.getWindow().menuBar.enableVaultMenuBarItems(false);
            FxApp.getApp().setTitle(null);
            FxApp.getWindow().statusBar.showStatus("");
        } catch (IOException e) {
            FxApp.getWindow().statusBar.showError("IO error when saving vault: " + e.getMessage());
        }
    }

    // MODIFIES: this, FxApp.getWindow()
    // EFFECTS: adds file to vault and updates list
    protected void addFile(File file) {
        try {
            vault.addFile(file, currentDir);
            updateList(currentDir);
            FxApp.getWindow().statusBar.showStatus("Added file \"" + file.getName() + "\" under \"/"
                    + vault.getRoot().getPathOfEntry(currentDir.getId(), true) + "\"");
        } catch (IOException e) {
            FxApp.getWindow().statusBar.showError("IO error when adding file: " + e.getMessage());
        } catch (CryptoException e) {
            FxApp.getWindow().statusBar.showError("Crypto error when adding file (maybe incorrect password?): "
                    + e.getMessage());
        }
    }

    // MODIFIES: FxApp.getWindow()
    // EFFECTS: prompts user to select destination directory and saves file
    protected void saveFile() {
        Optional<VaultEntry> vaultFile = currentDir.getEntries().stream()
                .filter(x -> x.getName().equals(list.getSelectionModel().selectedItemProperty().get())).findFirst();
        if (vaultFile.isPresent() && vaultFile.get().getClass().equals(VaultFile.class)) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Destination Folder");
            File destination = chooser.showDialog(null);
            if (destination == null) {
                return;
            }
            try {
                vault.saveFile(vaultFile.get().getName(), destination);
                FxApp.getWindow().statusBar.showStatus("Saved file \"" + vaultFile.get().getName() + "\" under \""
                        + destination.getAbsolutePath() + "\"");
            } catch (IOException e) {
                FxApp.getWindow().statusBar.showError("IO error when saving file: " + e.getMessage());
            } catch (CryptoException e) {
                FxApp.getWindow().statusBar.showError("Crypto error when saving file (incorrect password?): "
                        + e.getMessage());
            }
        } else {
            FxApp.getWindow().statusBar.showError("No file selected");
        }
    }

    // MODIFIES: this, FxApp.getWindow()
    // EFFECTS: creates new VaultDirectory under currentDir with name and updates list
    protected void createDir(String name) {
        try {
            vault.createDir(name, currentDir);
            updateList(currentDir);
            FxApp.getWindow().statusBar.showStatus("Created new folder \"" + name + "\" under \"/"
                    + vault.getRoot().getPathOfEntry(currentDir.getId(), true) + "\"");
        } catch (IOException e) {
            FxApp.getWindow().statusBar.showError("IO error when creating folder: " + e.getMessage());
        }
    }

    // MODIFIES: this
    // EFFECTS: sets current path and repopulates list of files
    private void updateList(VaultDirectory dir) {
        currentDir = dir;
        address.setText("/" + vault.getRoot().getPathOfEntry(currentDir.getId(), true));
        list.getItems().clear();
        list.getItems().addAll(
                dir.getEntries().stream().map(VaultEntry::getName).collect(Collectors.toList()));
    }

    // MODIFIES: this
    // EFFECTS: enables or disables all VaultExplorer elements relevant to a loaded vault
    private void enableVaultExplorerUI(boolean state) {
        back.setDisable(!state);
        address.setDisable(!state);
        if (!state) {
            address.setText("");
            list.getItems().clear();
        }
    }

}
