package clix.manager.resolver.impl;

import clix.Argument;
import clix.annotations.ResolverType;
import clix.manager.resolver.ParserTypeResolver;

import java.lang.reflect.Type;
import java.util.List;

@ResolverType(Argument.class)
public class ArgumentResolver implements ParserTypeResolver {

    @Override
    public Type value() {
        return Argument.class;
    }

    @Override
    public Object resolver(Argument value) {
        return value;
    }

    @Override
    public Object resolverTypes(Type[] types, List<Argument> value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
