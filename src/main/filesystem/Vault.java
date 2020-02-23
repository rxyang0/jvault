package filesystem;

import com.google.gson.JsonObject;
import exceptions.CryptoException;
import io.Reader;
import io.Writer;
import util.CryptoProvider;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private static final String ROOT_ID = "00000000-0000-0000-0000-000000000000";
    private static final String ROOT_NAME = "root";

    private File vaultFolder;
    private File dataFolder;
    private JsonObject index;       // Represents contents of .jvault JSON file
    protected VaultDirectory root;
    protected CryptoProvider crypto;

    // EFFECTS: loads existing vault filesystem, or creates new vault if folder does not exist
    public Vault(File vaultFolder, char[] password) throws IOException, CryptoException {
        this.vaultFolder = vaultFolder;
        dataFolder = new File(vaultFolder, "data");
        File jvault = new File(vaultFolder, vaultFolder.getName() + ".jvault");
        root = new VaultDirectory(ROOT_ID, ROOT_NAME);
        if (jvault.exists()) {
            index = new Reader(jvault).readJson();
            unlock(password);
            root.addEntries(index.getAsJsonObject("filesystem").getAsJsonArray("entries"));
        } else {
            dataFolder.mkdirs();
            unlock(password);
            save();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes CryptoProvider with password and if present, salt from filesystem
    protected void unlock(char[] password) throws CryptoException {
        if (index != null) {
            JsonObject cryptoParams = index.getAsJsonObject("crypto");
            crypto = new CryptoProvider(password, Base64.getDecoder().decode(cryptoParams.get("salt").getAsString()));
        } else {
            crypto = new CryptoProvider(password);
        }
    }

    // MODIFIES: this
    // EFFECTS: destroys CryptoProvider and saves filesystem
    public void lock() throws IOException {
        save();
        crypto.destroy();
    }

    // MODIFIES: dir
    // EFFECTS: add encrypted contents of input file to vault directory
    public void addFile(File inputFile, VaultDirectory dir) throws IOException, CryptoException {
        String fileName = inputFile.getName();

        byte[] encrypted = crypto.encrypt(new Reader(inputFile).readBytes());
        String id = UUID.randomUUID().toString();

        String pathFromRoot = dataFolder.getPath() + "/" + root.getPathOfEntry(dir.getId());
        new Writer(new File(pathFromRoot, id)).writeBytes(encrypted);

        VaultFile file = new VaultFile(id, fileName, (int) inputFile.length());
        dir.addEntry(file);

        save();
    }

    // MODIFIES: this
    // EFFECTS: finds and deletes file from vault root and disk
    public void deleteFile(String fileName) throws IOException {
        for (VaultEntry entry : root.getEntries()) {
            if (entry.getName().equals(fileName)) {
                File encrypted = new File(dataFolder, root.getPathOfEntry(entry.getId()));
                if (encrypted.exists()) {
                    encrypted.delete();
                }
                root.deleteEntry(entry);
                break;
            }
        }

        save();
    }

    // EFFECTS: decrypts and saves contents of file in vault root to local folder
    public void saveFile(String fileName, File outputDir) throws IOException, CryptoException {
        for (VaultEntry entry : root.getEntries()) {
            if (entry.getName().equals(fileName)) {
                File encrypted = new File(dataFolder, root.getPathOfEntry(entry.getId()));
                byte[] decrypted = crypto.decrypt(new Reader(encrypted).readBytes());
                new Writer(new File(outputDir, fileName)).writeBytes(decrypted);
                break;
            }
        }
    }

    // EFFECTS: saves filesystem data of this vault to filesystem.json in root folder
    public void save() throws IOException {
        index = new JsonObject();
        index.add("crypto", crypto.toJson());
        index.add("filesystem", root.toJson());
        new Writer(new File(vaultFolder, vaultFolder.getName() + ".jvault")).writeJson(index);
    }

    public File getVaultFolder() {
        return vaultFolder;
    }

    public JsonObject getIndex() {
        return index;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public VaultDirectory getRoot() {
        return root;
    }

}
