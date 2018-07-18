package utils;

public interface ThrowableSupplier<T> {
    T get() throws Exception;
}
