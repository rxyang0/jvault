package ui;

import exceptions.CryptoException;
import filesystem.Vault;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

// User interface when run from console
public class ConsoleApp {
    /*
     * Parameter specification:
     *      -p      <password>                              specifies password (required)
     *
     *      -c      <path to new vault>                     create new vault
     *
     *      -v      <path to vault>                         loads existing vault for one of the following options
     *      -l      <path in vault>                         lists entries in vault path
     *      -a      <path to input file>                    adds file to vault
     *      -d      <path of file in vault>                 deletes file from vault
     *      -s      <path of file in vault>                 saves file from vault to local drive
     */

    // EFFECTS: proceed with operations if arguments are provided, or exit
    public ConsoleApp(String[] args) {
        CommandLine params = parseArgs(defineOptions(), args);

        if (!params.hasOption("p")) {
            System.out.println("No password");
            System.exit(1);
        } else if (params.hasOption("c")) {
            handleCreateVault(params.getOptionValue("p").toCharArray(), params.getOptionValue("c"));
        } else if (params.hasOption("v")) {
            Vault vault = handleLoadVault(params.getOptionValue("p").toCharArray(), params.getOptionValue("v"));
            determineVaultOperation(params, vault);
        } else {
            System.out.println("No operation");
            System.exit(1);
        }
    }

    // EFFECTS: defines options for parsing command line
    private Options defineOptions() {
        Option password = new Option("p", true, "password");

        OptionGroup operations = new OptionGroup();
        operations.addOption(new Option("c", true, "create"));
        operations.addOption(new Option("v", true, "load vault"));

        OptionGroup vaultOperations = new OptionGroup();
        vaultOperations.addOption(new Option("l", true, "list"));
        vaultOperations.addOption(new Option("a", true, "add"));
        vaultOperations.addOption(new Option("d", true, "delete"));
        Option save = new Option("s", true, "save");
        save.setArgs(2);
        vaultOperations.addOption(save);

        Options options = new Options();
        options.addOption(password);
        options.addOptionGroup(operations);
        options.addOptionGroup(vaultOperations);

        return options;
    }

    // EFFECTS: parses arguments with defined options
    private CommandLine parseArgs(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    // EFFECTS: handles specified vault operation from parameters
    private void determineVaultOperation(CommandLine params, Vault vault) {
//        if (params.hasOption("l")) {
//        }
        if (params.hasOption("a")) {
            handleAddFile(vault, params.getOptionValue("a"));
        } else if (params.hasOption("d")) {
            handleDeleteFile(vault, params.getOptionValue("d"));
        } else if (params.hasOption("s")) {
            handleSaveFile(vault, params.getOptionValues("s")[0], params.getOptionValues("s")[1]);
        } else {
            System.out.println("No operation for vault");
        }
        try {
            vault.lock();
        } catch (IOException e) {
            System.out.println("Error when saving vault filesystem");
        }
    }

    // EFFECTS: handles creating a vault
    private void handleCreateVault(char[] password, String pathToVault) {
        File vaultFolder = new File(pathToVault);
        if (vaultFolder.exists()) {
            System.out.println("Folder already exists");
            System.exit(1);
        }
        try {
            new Vault(vaultFolder, password);
        } catch (IOException | CryptoException e) {
            System.out.println("Error in vault creation");
            e.printStackTrace();
        }
        System.out.println("Created new vault named \"" + vaultFolder.getName() + "\" at \""
                + vaultFolder.getAbsolutePath() + "\"");
    }

    // EFFECTS: handles loading a vault
    private Vault handleLoadVault(char[] password, String pathToVault) {
        File vaultFolder = new File(pathToVault);
        if (!vaultFolder.exists()) {
            System.out.println("Folder does not exist");
            System.exit(1);
        }
        Vault vault = null;
        try {
            vault = new Vault(vaultFolder, password);
        } catch (IOException | CryptoException e) {
            System.out.println("Error in loading vault");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Loaded vault \"" + vaultFolder.getName() + "\" at \""
                + vaultFolder.getAbsolutePath() + "\"");
        return vault;
    }

    // EFFECTS: handles adding a file to a vault
    private void handleAddFile(Vault vault, String fileName) {
        try {
            File file = new File(fileName);
            vault.addFile(file, vault.getRoot());
            System.out.println("Added file \"" + file.getAbsolutePath() + "\" to root of vault \""
                    + vault.getVaultFolder().getName() + "\"");
        } catch (IOException e) {
            System.out.println("Error when reading/writing file");
        } catch (CryptoException e) {
            System.out.println("Error in encryption");
        }
    }

    // EFFECTS: handles deleting a file from a vault
    private void handleDeleteFile(Vault vault, String fileName) {
        try {
            vault.deleteFile(fileName);
            System.out.println("Deleted file \"" + fileName + "\" from root of vault \""
                    + vault.getVaultFolder().getName() + "\"");
        } catch (IOException e) {
            System.out.println("Error when reading/writing file");
        }
    }

    // EFFECTS: handles saving a file from a vault to a local folder
    private void handleSaveFile(Vault vault, String fileName, String outputFolder) {
        try {
            File outputDir = new File(outputFolder);
            vault.saveFile(fileName, outputDir);
            System.out.println("Saved file \"" + fileName + "\" from root of vault \""
                    + vault.getVaultFolder().getName() + "\" to \"" + outputDir.getAbsolutePath() + "\"");
        } catch (IOException e) {
            System.out.println("Error when reading/writing file");
        } catch (CryptoException e) {
            System.out.println("Error in encryption");
        }
    }

}
