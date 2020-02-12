package com.yangrichard.ui;

import org.apache.commons.cli.*;

// User interface when run from console
public class ConsoleApp {
    /*
     * Parameter specification:
     *      -p  <password>                                              specifies password (required)
     *      -e  <path to input file> -o <path to output file>           encrypt input file and save to output file
     *      -d  <path to input file> -o <path to output file>           decrypt input file and save to output file
     *      -u  <path to file or vault directory> <new password>        re-encrypt file with new password
     *      -c  <path to vault directory>                               create new vault
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

        Options options = new Options();
        options.addOption(pass);
        options.addOptionGroup(operations);
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

    // REQUIRES: if params has option "e" or params has option "d", then params must have option "o"
    // EFFECTS: handle specified operation from arguments
    private void determineOperation(CommandLine params) {
        if (params.hasOption("e")) {
            handleEncryption(params.getOptionValue("p").toCharArray(), params.getOptionValue("e"),
                    params.getOptionValue("o"));
        } else if (params.hasOption("d")) {
            handleDecryption(params.getOptionValue("p").toCharArray(), params.getOptionValue("d"),
                    params.getOptionValue("o"));
        } else if (params.hasOption("u")) {
            handleUpdate(params.getOptionValue("p").toCharArray(), params.getOptionValues("u")[0],
                    params.getOptionValues("u")[1].toCharArray());
        } else if (params.hasOption("c")) {
            handleCreateVault(params.getOptionValue("p").toCharArray(), params.getOptionValue("c"));
        } else {
            System.out.println("No operation");
            System.exit(1);
        }
    }

    // EFFECTS: handles encryption with provided argument list
    private void handleEncryption(char[] password, String inputPath, String outputPath) {

    }

    // EFFECTS: handles decryption with provided argument list
    private void handleDecryption(char[] password, String inputPath, String outputPath) {

    }

    // EFFECTS: handles changing password of file/vault with provided argument list
    private void handleUpdate(char[] password, String inputPath, char[] newPassword) {

    }

    // EFFECTS: handles creation of vault with provided argument list
    private void handleCreateVault(char[] password, String pathToVault) {

    }

}
