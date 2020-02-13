package com.yangrichard.ui;

import com.yangrichard.filesystem.Vault;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

// User interface when run from console
public class ConsoleApp {
    /*
     * Parameter specification:
     *      -p  <password>                                                  specifies password (required)
     *      -e  <path to input file>  -o <path to output file>              encrypt input file and save to output file
     *      -d  <path to input file>  -o <path to output file>              decrypt input file and save to output file
     *      -u  <path to file or vault directory> <new password>            re-encrypt file with new password
     *      -c  <path to new vault>                                         create new vault
     *
     *      -l  <path to vault>                                             loads existing vault for following options
     *      -a <path to input file>     -o <relative path in vault>         adds input file to vault and encrypts
     *      -d <relative path in vault>                                     deletes file from vault
     *      -s <relative path in vault> -o <path to output file>            saves file from vault to local drive
     */

    // EFFECTS: proceed with operations if arguments are provided, or exit
    public ConsoleApp(String[] args) {
        CommandLine params = parseArgs(defineOptions(), args);
        if (params.getOptions().length == 0) {
            System.out.println("No arguments");
            System.exit(1);
        } else if (! params.hasOption("p")) {
            System.out.println("No password specified");
            System.exit(1);
        } else if ((params.hasOption("e") || params.hasOption("d")) && !params.hasOption("o")) {
            System.out.println("No output file specified");
            System.exit(1);
        } else if ((params.hasOption("a") || (params.hasOption("d")) || (params.hasOption("s")))
                && !params.hasOption("l")) {
            System.out.println("No vault specified");
            System.exit(1);
        }

        determineOperation(params);
    }

    // EFFECTS: defines options for parsing command line
    private Options defineOptions() {
        Option pass = new Option("p", true, "password");
        Option output = new Option("o", true,"output");

        OptionGroup operations = new OptionGroup();
        operations.addOption(new Option("e", true, "encrypt"));
        operations.addOption(new Option("d", true, "decrypt"));
        Option update = new Option("u", true, "update");
        update.setArgs(2);
        operations.addOption(update);
        operations.addOption(new Option("c", true, "create"));
        operations.addOption(new Option("l", true, "load"));

        OptionGroup vaultOperations = new OptionGroup();
        vaultOperations.addOption(new Option("a", true, "add"));
        vaultOperations.addOption(new Option("d", true, "delete"));
        vaultOperations.addOption(new Option("s", true, "save"));

        Options options = new Options();
        options.addOption(pass);
        options.addOptionGroup(operations);
        options.addOptionGroup(vaultOperations);
        options.addOption(output);

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

    // EFFECTS: handle specified operation from arguments
    private void determineOperation(CommandLine params) {
        if (params.hasOption("e")) {
            handleEncrypt(params.getOptionValue("p").toCharArray(), params.getOptionValue("e"),
                    params.getOptionValue("o"));
        } else if (params.hasOption("d")) {
            handleDecrypt(params.getOptionValue("p").toCharArray(), params.getOptionValue("d"),
                    params.getOptionValue("o"));
        } else if (params.hasOption("u")) {
            handleUpdate(params.getOptionValue("p").toCharArray(), params.getOptionValues("u")[0],
                    params.getOptionValues("u")[1].toCharArray());
        } else if (params.hasOption("c")) {
            handleCreateVault(params.getOptionValue("p").toCharArray(), params.getOptionValue("c"));
        } else if (params.hasOption("l")) {
            Vault vault = handleLoadVault(params.getOptionValue("p").toCharArray(), params.getOptionValue("l"));
            determineVaultOperation(params, vault);
        } else {
            System.out.println("No operation");
        }
        System.exit(1);
    }

    // EFFECTS: handles specified vault operation from parameters
    private void determineVaultOperation(CommandLine params, Vault vault) {
        if (params.hasOption("a")) {
            vault.addFile(new File(params.getOptionValue("a")), vault.getRoot());
        } else if (params.hasOption("d")) {
            System.exit(0);
        } else if (params.hasOption("s")) {
            System.exit(0);
        } else {
            System.out.println("No operation for vault");
        }
    }

    // EFFECTS: handles encryption with provided argument list
    private void handleEncrypt(char[] password, String inputPath, String outputPath) {
    }

    // EFFECTS: handles decryption with provided argument list
    private void handleDecrypt(char[] password, String inputPath, String outputPath) {
    }

    // EFFECTS: handles changing password of file/vault with provided argument list
    private void handleUpdate(char[] password, String inputPath, char[] newPassword) {
    }

    // EFFECTS: handles creation of vault with provided argument list
    private void handleCreateVault(char[] password, String pathToVault) {
        File vaultFolder = new File(pathToVault);
        if (vaultFolder.exists()) {
            System.out.println("Folder already exists. Abort.");
            System.exit(1);
        }
        try {
            new Vault(vaultFolder, password);
        } catch (IOException e) {
            System.out.println("Error in vault creation");
            e.printStackTrace();
        }
        System.out.println("Created new vault named " + vaultFolder.getName() + " at " + vaultFolder.getAbsolutePath());
    }

    // EFFECTS: handles loading a vault
    private Vault handleLoadVault(char[] password, String pathToVault) {
        File vaultFolder = new File(pathToVault);
        if (!vaultFolder.exists()) {
            System.out.println("Folder does not exist. Abort.");
            System.exit(1);
        }
        Vault vault = null;
        try {
            vault = new Vault(vaultFolder, password);
        } catch (IOException e) {
            System.out.println("Error in loading vault");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Loaded vault named " + vaultFolder.getName() + " at " + vaultFolder.getAbsolutePath());
        return vault;
    }

}
