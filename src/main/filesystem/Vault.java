package filesystem;

import com.google.gson.JsonObject;
import exceptions.CryptoException;
import io.Reader;
import io.Writer;
import org.apache.commons.io.FileUtils;
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
            boolean result = dataFolder.mkdirs();
            unlock(password);
            sync();
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
        sync();
        crypto.destroy();
    }

    // MODIFIES: dir
    // EFFECTS: add encrypted contents of input file to vault directory
    public void addFile(File inputFile, VaultDirectory dir) throws IOException, CryptoException {
        String fileName = inputFile.getName();

        byte[] encrypted = crypto.encrypt(new Reader(inputFile).readBytes());
        String id = UUID.randomUUID().toString();

        String pathFromRoot = dataFolder.getPath() + "/" + root.getPathOfEntry(dir.getId(), false);
        new Writer(new File(pathFromRoot, id)).writeBytes(encrypted);

        VaultFile file = new VaultFile(id, fileName, (int) inputFile.length());
        dir.addEntry(file);

        sync();
    }

    // MODIFIES: this
    // EFFECTS: deletes file or folder from dir and disk
    public void delete(VaultEntry entry, VaultDirectory dir) throws IOException {
        if (entry.getClass().equals(VaultFile.class)) {
            boolean result = new File(dataFolder, root.getPathOfEntry(entry.getId(), false)).delete();
        } else {
            FileUtils.deleteDirectory(new File(dataFolder, root.getPathOfEntry(entry.getId(), false)));
        }
        dir.deleteEntry(entry);

        sync();
    }

    // EFFECTS: decrypts and saves contents of file in vault root to local folder
    public void saveFile(String fileName, File outputDir) throws IOException, CryptoException {
        for (VaultEntry entry : root.getEntries()) {
            if (entry.getName().equals(fileName)) {
                File encrypted = new File(dataFolder, root.getPathOfEntry(entry.getId(), false));
                byte[] decrypted = crypto.decrypt(new Reader(encrypted).readBytes());
                new Writer(new File(outputDir, fileName)).writeBytes(decrypted);
                break;
            }
        }
    }

    // EFFECTS: decrypts and returns contents of file
    public byte[] save(VaultEntry entry) throws IOException, CryptoException {
        if (entry.getClass().equals(VaultFile.class)) {
            File encrypted = new File(dataFolder, root.getPathOfEntry(entry.getId(), false));
            return crypto.decrypt(new Reader(encrypted).readBytes());
        } else {
            return new byte[0];
        }
    }

    // MODIFIES: this
    // EFFECTS: creates new directory under existing VaultDirectory
    public void createDir(String name, VaultDirectory parent) throws IOException {
        String id = UUID.randomUUID().toString();
        String pathFromRoot = dataFolder.getPath() + "/" + root.getPathOfEntry(parent.getId(), false);
        boolean result = new File(pathFromRoot, id).mkdir();
        parent.addEntry(new VaultDirectory(id, name));
        sync();
    }

    // EFFECTS: saves filesystem data of this vault to filesystem.json in root folder
    public void sync() throws IOException {
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
