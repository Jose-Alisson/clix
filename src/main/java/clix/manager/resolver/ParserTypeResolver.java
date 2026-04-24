package clix.manager.resolver;

import clix.Argument;

import java.lang.reflect.Type;
import java.util.List;

public interface ParserTypeResolver {
    Type value();
    Object resolver(Argument value);
    Object resolverTypes(Type[] types, List<Argument> value);
}
