package clix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {

    Command command;

    public Parser(String... args) {
        if (args.length == 0) {
            throw new RuntimeException("Nenhum comando definido");
        }

        List<Argument> arguments = new ArrayList<>();
        HashMap<String, Flag> flagsMap = new HashMap<>();

        for (int index = 1; index < args.length; index++) {
            var starts = args[index].startsWith("-");
            if (starts) {
                var split = args[index].split("=");
                var flag = split[0].replace("-", "");
                var value = new Flag(flag, split.length > 1 ? split[1] : null);
                flagsMap.put(flag, value);
            } else {
                Argument argument = new Argument(args[index]);
                arguments.add(argument);
            }
        }
        this.command = new Command(args[0], arguments, flagsMap);
    }

    public Command getCommand() {
        return command;
    }
}
