package clix.manager.resolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class ParserTypeResolverManager {

    List<ParserTypeResolver> parserTypeResolvers;

    public ParserTypeResolverManager(Set<Class<?>> resolverTypes) {
        parserTypeResolvers = resolverTypes.stream()
                .filter(c -> ParserTypeResolver.class.isAssignableFrom(c) && !c.isInterface())
                .map(c -> {
                            try {
                                return ((ParserTypeResolver) c.getDeclaredConstructor().newInstance());
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).toList();
    }

    public ParserTypeResolver get(Type type) {
        for (ParserTypeResolver parserTypeResolver : parserTypeResolvers) {
            if (parserTypeResolver.value() == type) {
                return parserTypeResolver;
            }
        }
        throw new IllegalArgumentException("The Resolver by type %s not found".formatted(type.getTypeName()));
    }
}
