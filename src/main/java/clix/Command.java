package clix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command {

    private String command;
    private List<Argument> arguments = new ArrayList<>();
    private HashMap<String, Flag> flags = new HashMap<>();

    private CommandAction action;

    public Command(String command, List<Argument> arguments, HashMap<String,Flag> flags){
        this.command = command;
        this.arguments = arguments;
        this.flags = flags;
    }

    public Command() {}

    public String getCommand() {
        return command;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public HashMap<String, Flag> getFlags() {
        return flags;
    }

    public CommandAction getAction() {
        return action;
    }

    public interface CommandAction{
        void action();
    }

    public static class SubCommand extends Command { }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    public void setFlags(HashMap<String, Flag> flags) {
        this.flags = flags;
    }

    public void setAction(CommandAction action) {
        this.action = action;
    }
}
