package filesystem;

import com.google.gson.JsonObject;
import io.Reader;
import io.Writer;
import util.CryptoProvider;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private File vaultFolder;
    private File dataFolder;
    private JsonObject filesystem;
    private VaultDirectory root;
    private CryptoProvider crypto;

    // EFFECTS: loads existing vault filesystem, or creates new vault if folder does not exist
    public Vault(File vaultFolder, char[] password) throws IOException {
        this.vaultFolder = vaultFolder;
        dataFolder = new File(vaultFolder, "data");
        File filesystemFile = new File(vaultFolder, "filesystem.json");
        if (filesystemFile.exists() && dataFolder.exists()) {
            filesystem = new Reader(filesystemFile).readJson();
            unlock(password);
            loadEntries(filesystem);
        } else {
            dataFolder.mkdirs();
            unlock(password);
            root = new VaultDirectory("0", vaultFolder.getName());
            save();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes CryptoProvider with password and if present, salt from filesystem
    protected void unlock(char[] password) {
        try {
            if (filesystem != null) {
                crypto = new CryptoProvider(password, filesystem.get("salt").getAsString().getBytes());
            } else {
                crypto = new CryptoProvider(password);
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: destroys CryptoProvider
    protected void lock() {
        crypto.destroy();
    }

    // REQUIRES: filesystem has member "entries"
    // MODIFIES: this
    // EFFECTS: loads filesystem entries from JSON into root directory
    private void loadEntries(JsonObject filesystem) {
        root = new VaultDirectory("0", filesystem.get("name").getAsString());
        root.addEntries(filesystem.get("entries").getAsJsonArray());
    }

    // MODIFIES: dir
    // EFFECTS: add encrypted contents of input file to vault directory
    public void addFile(File inputFile, VaultDirectory dir) throws Exception {
        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
        String extension = inputFile.getName().substring(inputFile.getName().lastIndexOf('.'));

        byte[] encrypted = crypto.encrypt(new Reader(inputFile).readBytes());
        String id = UUID.randomUUID().toString();

        new Writer(new File(dataFolder, id)).writeBytes(encrypted);

        VaultFile file = new VaultFile(id, fileName, extension, (int) inputFile.length());
        dir.addEntry(file);
    }

//    // EFFECTS: decrypts and saves contents of file in vault to output directory
//    public void saveFile(VaultFile file, File outputDir) {
//        File encrypted = new File(vault.getAbsolutePath() + "/" + root.getPathOfEntry(file));
//        // Decrypt and save to outputDir
//    }

    // EFFECTS: saves filesystem data of this vault to filesystem.json in root folder
    public void save() throws IOException {
        new Writer(new File(vaultFolder, "filesystem.json")).writeJson(root.toJson());
    }

    public VaultDirectory getRoot() {
        return root;
    }

}
