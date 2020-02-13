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

    private static final String rootID = "00000000-0000-0000-0000-000000000000";
    private static final String rootName = "root";

    protected File vaultFolder;
    protected File dataFolder;
    protected JsonObject filesystem;
    protected VaultDirectory root;
    protected CryptoProvider crypto;

    // EFFECTS: loads existing vault filesystem, or creates new vault if folder does not exist
    public Vault(File vaultFolder, char[] password) throws IOException, CryptoException {
        this.vaultFolder = vaultFolder;
        dataFolder = new File(vaultFolder, "data");
        File filesystemFile = new File(vaultFolder, "filesystem.json");
        root = new VaultDirectory(rootID, rootName);
        if (filesystemFile.exists() && dataFolder.exists()) {
            filesystem = new Reader(filesystemFile).readJson();
            unlock(password);
            root.addEntries(filesystem.get(rootName).getAsJsonObject().getAsJsonArray("entries"));
        } else {
            dataFolder.mkdirs();
            unlock(password);
            save();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes CryptoProvider with password and if present, salt from filesystem
    protected void unlock(char[] password) throws CryptoException {
        if (filesystem != null) {
            crypto = new CryptoProvider(password, Base64.getDecoder().decode(filesystem.get("salt").getAsString()));
        } else {
            crypto = new CryptoProvider(password);
        }
    }

    // MODIFIES: this
    // EFFECTS: destroys CryptoProvider
    protected void lock() {
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
    }

//    // EFFECTS: decrypts and saves contents of file in vault to output directory
//    public void saveFile(VaultFile file, File outputDir) {
//        File encrypted = new File(vault.getAbsolutePath() + "/" + root.getPathOfEntry(file));
//        // Decrypt and save to outputDir
//    }

    // EFFECTS: saves filesystem data of this vault to filesystem.json in root folder
    public void save() throws IOException {
        filesystem = new JsonObject();
        filesystem.addProperty("salt", Base64.getEncoder().encodeToString(crypto.getSalt()));
        filesystem.add(rootName, root.toJson());
        new Writer(new File(vaultFolder, "filesystem.json")).writeJson(filesystem);
    }

    public VaultDirectory getRoot() {
        return root;
    }

}
