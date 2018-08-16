/*
 * Copyright (C) 2018 AlertMe.com Ltd
 */
package reflection;

import com.google.common.collect.Streams;
import utils.Result;
import utils.Tuple;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class ReflectionUtils {

    public static <T> Result<T> createClass(final Class<T> clazz, final Object... arguments) {
        return findConstructor(clazz, arguments)
                .map(x -> Result.ofThrowable(() -> ((T) createInstance(x, arguments))))
                .orElse(Result.failure("No suitable constructor found for arguments "));
    }

    private static Object createInstance(final Constructor constructor, final Object... arguments)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(arguments);
    }


    static Optional<Constructor> findConstructor(final Class<?> clazz, final Object[] args) {
        final Constructor constructors[] = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(el -> compareArguments(el.getParameterTypes(), args))
                .findFirst();
    }

    static boolean compareArguments(final Class<?>[] params, final Object[] args) {
        if (params.length != args.length) {
            return false;
        }
        final Predicate<Tuple<Object, Class>> argumentPredicate =
                x -> x.left().getClass() != x.right() && x.left().getClass().isAssignableFrom(x.right());
        return Streams.zip(Arrays.stream(args),Arrays.stream(params),(x,y)->Tuple.of(x, primitiveToClass(y)))
                .filter(argumentPredicate)
                .findFirst()
                .map(x -> false)
                .orElse(true);
        /*for (int i = 0; i < params.length; i++) {
            final Class<?> paramClass = primitiveToClass(params[i]);
            final Class<?> argumentClass = args[i].getClass();
            if (argumentClass != paramClass && argumentClass.isAssignableFrom(paramClass)) {
                return false;
            }
        }
        return true;*/
    }

    /**
     * Converts primitive data types to class object
     *
     * @param type type to be converted
     * @return
     */
    static Class primitiveToClass(final Class<?> type) {
        switch (type.getTypeName()) {
            case "boolean":
                return Boolean.class;
            case "int":
                return Integer.class;
            case "float":
                return Float.class;
            case "Double":
                return Double.class;
            case "short":
                return Short.class;
            case "long":
                return Long.class;
            case "char":
                return Character.class;
            case "byte":
                return Byte.class;
            default:
                return type;
        }
    }
}
