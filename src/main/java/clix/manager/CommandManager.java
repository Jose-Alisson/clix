package clix.manager;

import clix.Argument;
import clix.Help;
import clix.Parser;
import clix.annotations.*;
import clix.manager.resolver.ParserTypeResolverManager;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;

public class CommandManager {

    public static Map<String, clix.Command> commands = new HashMap<>();
    public static ParserTypeResolverManager manager;

    private static boolean enabledHelp = false;

    public static void initialize(String pack) {
        Reflections reflect = new Reflections(pack);
        Set<Class<?>> classes = reflect.getTypesAnnotatedWith(Command.class);

        Set<Class<?>> resolvers = reflect.getTypesAnnotatedWith(ResolverType.class);
        manager = new ParserTypeResolverManager(resolvers);

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
                    method.setAccessible(true);
                    Object instance;

                    try {
                        instance = clazz.getConstructor().newInstance();
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    command.setAction(getAction(method, command, instance));
                }
            }
            commands.put(command.getCommand(), command);
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

            if (command.getArguments().size() < action.arguments().length) {
                System.err.printf("The command %s is missing arguments%n", command.getCommand());
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
                    values.add(manager.get(pt.getRawType()).resolverTypes(pt.getActualTypeArguments(), arguments));
                } else {
                    Parameter parameter = params[i];
                    if(parameter.isAnnotationPresent(Param.class)){
                        Param param = parameter.getAnnotation(Param.class);
                        int finalI = i;
                        Arrays.stream(action.arguments()).filter(a -> param.name().equals(a.name())).findFirst().ifPresent(value -> {
                            values.add(manager.get(parameter.getType()).resolver(arguments.get(finalI)));
                        });
                    } else {
                        values.add(manager.get(parameter.getType()).resolver(arguments.get(i)));
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
