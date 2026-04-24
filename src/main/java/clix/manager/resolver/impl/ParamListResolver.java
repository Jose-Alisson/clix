package clix.manager.resolver.impl;

import clix.Argument;
import clix.Flag;
import clix.annotations.ResolverType;
import clix.manager.resolver.ParserTypeResolver;
import clix.types.ArgumentType;

import java.lang.reflect.Type;
import java.util.List;

@ResolverType(List.class)
public class ParamListResolver implements ParserTypeResolver {

    @Override
    public Type value() {
        return List.class;
    }

    @Override
    public Argument resolver(Argument value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<?> resolverTypes(Type[] types, List<Argument> values) {
        if (types[0] == Argument.class) {
            return values;
        } else if (types[0] == Flag.class) {
            return values.stream().filter(a -> a.getType() == ArgumentType.FLAG).toList();
        } else if (types[0] == String.class) {
            return values.stream().map(Argument::getValue).toList();
        }
        throw new RuntimeException("The type " + types[0] + " is not supported");
    }
}
