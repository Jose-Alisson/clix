package clix.manager;

import clix.Argument;
import clix.Flag;
import clix.Help;
import clix.Parser;
import clix.annotations.*;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;

public class CommandManager {

    public static Map<String, clix.Command> commands = new HashMap<>();

    private static boolean enabledHelp = false;

    public static void initialize(String pack) {
        Reflections reflect = new Reflections(pack);
        Set<Class<?>> classes = reflect.getTypesAnnotatedWith(Command.class);

        if(!reflect.getTypesAnnotatedWith(EnableHelp.class).isEmpty()){
            classes.add(Help.class);
            enabledHelp = true;
        };

        for (Class<?> clazz : classes) {
            var ann = clazz.getAnnotation(Command.class);
            clix.Command command = new clix.Command();
            command.setCommand(ann.command());

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Action.class)) {
//                    Action ac = method.getAnnotation(Action.class);
//                    Command.SubCommand sub = new Command.SubCommand();
                    method.setAccessible(true);
                    Object instance;

                    try {
                        instance = clazz.getConstructor().newInstance();
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    command.setAction(getAction(method, command, instance));

//                    if (ac.subcommand().isEmpty()) {} else {
//                        sub.setCommand(ann.command() + ":" + ac.subcommand());
//                        sub.setAction(getAction(method, sub, instance));
//                        commands.put(sub.getCommand(), sub);
//                        command.setAction(getActionToListSubCommands(Arrays.stream(clazz.getDeclaredMethods())
//                                .filter(m -> m.isAnnotationPresent(Action.class))
//                                .toArray(Method[]::new)));
//                    }
                }
            }

//            if (commands.get(command.getCommand()) == null) {
            commands.put(command.getCommand(), command);
//            } else {
//                System.err.printf("The command %s is defined%n", command.getCommand());
//            }
        }
    }

    private static clix.Command.CommandAction getActionToListSubCommands(Method[] method) {
        return () -> {
        };
    }

    private static clix.Command.CommandAction getAction(Method method, clix.Command command, Object instance) {
        Action action = method.getAnnotation(Action.class);

        command.setArguments(Arrays.stream(action.arguments()).map(d -> new Argument(d.name(), d.description())).toList());

        return () -> {
            List<Object> values = new ArrayList<>();

            var arguments = command.getArguments();
            var flags = command.getFlags();

            if (command.getArguments().size() < action.arguments().length) {
                System.err.printf("Para o comando %s está faltando argumentos%n", command.getCommand());
                return;
            }

            for (RefactorArgument ref : action.refactor()) {
                for (Argument arg : arguments) {
                    arg.setName(arg.getName().replaceAll("^(%s)$".formatted(String.join("|", ref.params())), ref.refactor()));
                }
            }

            var params = method.getParameters();
            var typesParams = method.getGenericParameterTypes();

            for (int i = 0; i < params.length; i++) {
                if (typesParams[i] instanceof ParameterizedType pt) {
                    if (pt.getRawType() == List.class) {
                        if (pt.getActualTypeArguments()[0] == Argument.class) {
                            values.add(arguments);
                        } else if (pt.getActualTypeArguments()[0] == Flag.class) {
                            values.add(flags.values().stream().toList());
                        }
                    } else if (pt.getRawType() == HashMap.class || pt.getRawType() == Map.class) {
                        if (pt.getActualTypeArguments()[0] == String.class && pt.getActualTypeArguments()[1] == Flag.class) {
                            values.add(flags);
                        }
                    }
                } else if (params[i].getType() == String.class){
                    Parameter param = params[i];
                    if(param.isNamePresent()) {
                        var nameExist = false;
                        for (int j = 0; j < action.arguments().length; j++) {
                            if (param.getName().equals(action.arguments()[j].name())) {
                                nameExist = true;
                                values.add(arguments.get(j).getName());
                            }
                        }
                        if(!nameExist){
                            values.add(arguments.get(i).getName());
                        }
                    } else {
                        values.add(arguments.get(i).getName());
                    }
                } else if(params[i].getType() == Argument.class){
                    Parameter param = params[i];
                    if(param.isNamePresent()) {
                        var nameExist = false;
                        for (int j = 0; j < action.arguments().length; j++) {
                            if (param.getName().equals(action.arguments()[j].name())) {
                                nameExist = true;
                                values.add(new Argument(arguments.get(j).getName()));
                            }
                        }
                        if(!nameExist){
                            values.add(new Argument(arguments.get(i).getName()));
                        }
                    } else {
                        values.add(new Argument(arguments.get(i).getName()));
                    }
                }
            }

            try {
                method.invoke(instance, values.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e instanceof IllegalAccessException) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static void exec(clix.Command command) {
        clix.Command c = commands.get(command.getCommand());

        if (c == null) {
            if(enabledHelp) {
                System.err.println("Command is not defined, type help for list commands");
            }
            return;
        }

        c.setArguments(command.getArguments());
        c.setFlags(command.getFlags());

        try {
            c.getAction().action();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void exec(Parser parser) {
        exec(parser.getCommand());
    }
}
