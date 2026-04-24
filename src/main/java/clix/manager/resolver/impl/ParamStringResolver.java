package clix.manager.resolver.impl;

import clix.Argument;
import clix.Flag;
import clix.annotations.ResolverType;
import clix.manager.resolver.ParserTypeResolver;

import java.lang.reflect.Type;
import java.util.List;

@ResolverType(String.class)
public class ParamStringResolver implements ParserTypeResolver {

    @Override
    public Type value() {
        return String.class;
    }

    @Override
    public String resolver(Argument value) {
        return value.getValue();
    }

    @Override
    public Object resolverTypes(Type[] types, List<Argument> value) {
        throw new RuntimeException("String not resolver many types");
    }
}
