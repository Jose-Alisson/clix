package clix.x;

import clix.Argument;
import clix.annotations.Action;
import clix.annotations.Command;

@Command(command = "--version")
public class Version {

    @Action
    public void v(Argument argument) {

        System.out.println("1.0.1");
    }
}
