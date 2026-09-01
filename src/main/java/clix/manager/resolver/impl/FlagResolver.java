package clix.manager.resolver.impl;

import clix.Argument;
import clix.Flag;
import clix.annotations.ResolverType;
import clix.manager.resolver.ParserTypeResolver;
import clix.types.ArgumentType;

import java.lang.reflect.Type;
import java.util.List;

@ResolverType(Argument.class)
public class FlagResolver implements ParserTypeResolver {
    @Override
    public Type value() {
        return Flag.class;
    }

    @Override
    public Object resolver(Argument value) {
        if(value.getType() == ArgumentType.FLAG) {
            return value;
        }
        throw new RuntimeException("The Argument Type is not Flag");
    }

    @Override
    public Object resolverTypes(Type[] types, List<Argument> value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
