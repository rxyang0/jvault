package com.yangrichard.ui;

import java.util.List;

// User interface when run from console
public class ConsoleApp {

    // EFFECTS: proceed with operations if arguments are provided, or exit
    public ConsoleApp(List<String> args) {
        if (args.size() > 0) {
            determineOperation(args);
        } else {
            System.out.println("No arguments");
        }
    }

    // EFFECTS: determines operation from argument list and calls appropriate helper method
    private void determineOperation(List<String> args) {
        String operation = args.get(0);
        args.remove(0);
        switch (operation) {
            case "-e":
                handleEncryption(args);
                break;
            case "-d":
                handleDecryption(args);
                break;
            case "-u":
                handleUpdate(args);
                break;
            case "-c":
                handleCreateVault(args);
                break;
            default:
                System.out.println("Invalid operation");
        }
    }

    // EFFECTS: handles encryption with provided argument list
    private void handleEncryption(List<String> encryptionArgs) {

    }

    // EFFECTS: handles decryption with provided argument list
    private void handleDecryption(List<String> decryptionArgs) {

    }

    // EFFECTS: handles changing password of file/vault with provided argument list
    private void handleUpdate(List<String> updateArgs) {

    }

    // EFFECTS: handles creation of vault with provided argument list
    private void handleCreateVault(List<String> createVaultArgs) {

    }

}
