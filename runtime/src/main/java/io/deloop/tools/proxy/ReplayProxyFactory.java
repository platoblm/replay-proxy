package io.deloop.tools.proxy;

import static java.lang.String.format;

public class ReplayProxyFactory {

    public static final String GENERATED_CLASS_SUFFIX = "_ReplayProxy";

    public static <T> ReplayProxy<T> createFor(Class<T> type) {
        T instance = (T) newInstanceFor(type);
        return (ReplayProxy<T>) instance;
    }

    private static String expectedGeneratedClassName(Class<?> type) {
        return type.getPackage().getName() + "." + type.getSimpleName() + GENERATED_CLASS_SUFFIX;
    }

    private static Object newInstanceFor(Class<?> type) {
        try {
            String className = expectedGeneratedClassName(type);
            return Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            throw error(type, e);
        } catch (IllegalAccessException e) {
            throw error(type, e);
        } catch (ClassNotFoundException e) {
            throw error(type, e);
        }
    }

    private static RuntimeException error(Class<?> type, Throwable e) {
        String message = format("Can't create a proxy for %s. Have you annotated it with %s?",
                type.getCanonicalName(), HasReplayProxy.class.getSimpleName());

        return new RuntimeException(message, e);
    }
}
