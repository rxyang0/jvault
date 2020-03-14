package ui;

import javafx.application.Application;

import java.util.Arrays;

// Entry point into program
public class Main {

    // EFFECTS: start GUI if graphics environment is available, or run console interface otherwise
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-nogui")) {
            new ConsoleApp(Arrays.copyOfRange(args, 1, args.length));       // start console interface
        } else {
            Application.launch(FxApp.class, args);
        }
    }

}
