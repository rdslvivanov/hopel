package interfaces;

public interface TeamBuilder {
    void add(final String name, final Class<?> serviceClass, final Object... parameters);
}
