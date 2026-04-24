package clix.x;

import clix.annotations.Action;
import clix.annotations.Command;

@Command(command = "--version")
public class Version {

    @Action
    public void v() {
        System.out.println("1.0.1");
    }
}
