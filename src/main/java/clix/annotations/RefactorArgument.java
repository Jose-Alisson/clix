package clix.annotations;

public @interface RefactorArgument {
    String[] params();
    String refactor();
}
