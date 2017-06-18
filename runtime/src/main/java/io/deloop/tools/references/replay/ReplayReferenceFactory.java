package io.deloop.tools.references.replay;

import static java.lang.String.format;

public class ReplayReferenceFactory {

    public static final String GENERATED_CLASS_SUFFIX = "_ReplayReference";

    public static <T> Reference<T> createFor(Class<T> type) {
        T instance = (T) createReferenceInstance(type);
        return (Reference<T>) instance;
    }

    private static String expectedGeneratedClassName(Class<?> type) {
        return type.getPackage().getName() + "." + type.getSimpleName() + GENERATED_CLASS_SUFFIX;
    }

    private static Object createReferenceInstance(Class<?> type) {
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
        String message = format("Can't create a Replay Reference for %s. Have you annotated it with %s?",
                type.getCanonicalName(), ReplayReference.class.getSimpleName());

        return new RuntimeException(message, e);
    }
}
