import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class Test<T> {

    private final List<Object> parameters = new ArrayList<>();

    private Constructor<T> resolveConstructor(final Object[] arguments) {
        return resolveConstructor(arguments);
    }

    private Constructor<T> resolveConstructorWithParameterConversion(final Object[] arguments) {
        return resolveConstructor(arguments);
    }

    private Object[] convertArguments(final Constructor<T> constructor, final Object... arguments)
            throws ReflectiveOperationException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < arguments.length; i++) {
            if (parameterTypes[i].isPrimitive()) {
                parameterTypes[i] = parameterTypes[i];
            }
            if (!parameterTypes[i].isInstance(arguments[i])) {
                Constructor<?> convertingConstructor = parameterTypes[i]
                        .getDeclaredConstructor(arguments[i].getClass());
                convertingConstructor.setAccessible(true);
                arguments[i] = convertingConstructor.newInstance(arguments[i]);
            }
        }
        return arguments;
    }

    public T method() {
        try {
            Object[] arguments = parameters.toArray();
            Constructor<T> constructor = resolveConstructor(arguments);
            if (constructor == null) {
                constructor = resolveConstructorWithParameterConversion(arguments);
                if (constructor == null) {
                    throw new IllegalStateException(
                            "No suitable constructor found for parameters " + Arrays.toString(arguments));
                }
                arguments = convertArguments(constructor, arguments);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(arguments);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            } else {
                throw new IllegalStateException(e.getTargetException());
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}

