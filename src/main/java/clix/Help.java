package clix;

import clix.annotations.Action;
import clix.annotations.Command;
import clix.manager.CommandManager;

import java.util.HashMap;
import java.util.List;

@Command(command = "help")
public class Help {

    @Action
    public void listCommands(List<Argument> arguments, HashMap<String, Flag> flags) {
            System.out.println(
                    """
                            USO: CLI [comando] [Argumentos] [Opções ou Flags]
                            
                            COMANDOS DISPONÍVEIS:
                            """);
            for (clix.Command command : CommandManager.commands.values()) {
                System.out.printf("%-15s %s%s%n", command.getCommand(), String.join(", ", command.getArguments().stream().map(Argument::getName).toList()), String.join(",", command.getFlags().values().stream().map(f -> "--" + f.getFlag()).toList()));
            }
    }


    //+ " | " + command.getArguments().toString().replace("[", "").replace("]", "") + " | " + command.getFlags().toString().replace("[", "").replace("]", "")
}
