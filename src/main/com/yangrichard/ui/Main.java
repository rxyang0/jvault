package com.yangrichard.ui;

import java.awt.*;
import java.util.Arrays;

// Entry point into program
public class Main {

    // EFFECTS: start GUI if graphics environment is available, or run console interface otherwise
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.exit(0);                 // no GUI implemented yet
        } else {
            new ConsoleApp(args);                   // start console interface
        }
    }

}
