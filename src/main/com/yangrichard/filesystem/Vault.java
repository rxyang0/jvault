package com.yangrichard.filesystem;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private String pathToVault;
    private VaultDirectory root;

    // EFFECTS: creates new vault filesystem with path in host filesystem and empty root
    public Vault(String pathToVault) {
        this.pathToVault = pathToVault;
        root = new VaultDirectory();
    }

    public String getPathToVault() {
        return pathToVault;
    }

}
