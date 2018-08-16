package utils;

public class Tuple<T, V> {
    private final T left;
    private final V right;

    private Tuple(final T left, final V right) {
        this.left = left;
        this.right = right;
    }

    public static <T, V> Tuple<T, V> of(final T left, final V right) {
        return new Tuple<>(left, right);
    }

    public T left() {
        return left;
    }

    public V right() {
        return right;
    }
}
