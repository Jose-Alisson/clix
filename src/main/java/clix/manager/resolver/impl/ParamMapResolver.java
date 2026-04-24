package clix.manager.resolver.impl;

import clix.Argument;
import clix.Flag;
import clix.annotations.ResolverType;
import clix.manager.resolver.ParserTypeResolver;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ResolverType(Map.class)
public class ParamMapResolver implements ParserTypeResolver {
    @Override
    public Type value() {
        return null;
    }

    @Override
    public Argument resolver(Argument value) {
        return null;
    }

    @Override
    public Map<String, ?> resolverTypes(Type[] types, List<Argument> value) {
        if(types[0] == String.class){
            if(types[1] == String.class || types[1] == Flag.class){
                return value.stream().collect(Collectors.toMap(
                        Argument::getName, Argument::getValue
                ));
            }
        }
        throw new RuntimeException("String not resolver many types");
    }
}
