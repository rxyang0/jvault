package com.yangrichard.filesystem;

import java.util.ArrayList;

// Represents a directory entry that can either be the root directory of the vault, or a subdirectory
public class VaultDirectory extends VaultEntry {

    private ArrayList<VaultEntry> entries;

    // EFFECTS: constructs new directory with given name and zero size
    public VaultDirectory(String name, String encryptedName) {
        super(name, encryptedName);
        entries = new ArrayList<>();
        this.size = 0;
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to the directory and updates the size
    public void addEntry(VaultEntry entry) {
        entries.add(entry);
        updateSize();
    }

    // MODIFIES: this
    // EFFECTS: deletes an entry from the directory and updates the size
    public void deleteEntry(VaultEntry entry) {
        entries.remove(entry);
        updateSize();
    }

    // MODIFIES: this
    // EFFECTS: calculates (recursively on directories) the total size of all entries in directory
    public void updateSize() {
        int total = 0;
        for (VaultEntry entry : entries) {
            total += entry.size();
        }
        size = total;
    }

    public ArrayList<VaultEntry> getEntries() {
        return entries;
    }

    // EFFECTS: recursively finds path of entry relative to current directory
    public String getPathOfEntry(VaultEntry target) {
        for (VaultEntry entry : entries) {
            if (entry.getClass().equals(VaultDirectory.class)) {
                return entry.getEncryptedName() + "/" + ((VaultDirectory) entry).getPathOfEntry(target);
            } else {
                if (entry.equals(target)) {
                    return entry.getEncryptedName();
                }
            }
        }
        return null;
    }

}
