package clix;

import clix.manager.CommandManager;

public class Main {
    public static void main(String[] args) {
        CommandManager.initialize("clix");
        CommandManager.exec(new Parser(args));
    }
}