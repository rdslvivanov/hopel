package utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Result<T> {
    private Result() {
    }

    public static <T> Result<T> success(final T t) {
        return new Success<>(t);
    }

    public static <T> Result<T> failure(final Exception t) {
        return new Failure<>(t);
    }

    public static <T> Result<T> failure(final String t) {
        return new Failure<>(t);
    }


    public static <T> Result<T> ofThrowable(final ThrowableSupplier<T> t) {
        try {
            return new Success(t.get());
        } catch (Exception e) {
            return new Failure(e);
        }
    }

    public static <T> Result<T> ofNullable(final T t, final String error) {
        return t == null
                ? new Failure<>(error)
                : new Success<>(t);
    }

    public abstract Optional<Exception> executeOrGetError(final Consumer<T> consumer);

    public abstract void executeOrThrow(final Consumer<T> consumer);

    public abstract <U> Result<U> map(final Function<T, U> f);

    public abstract <U> Result<U> flatMap(final Function<T, Result<U>> f);

    public abstract void bind(final Consumer<T> success, final Consumer<Exception> failure);
    public abstract Result<T> mapFailure(String s);
    public abstract boolean isSuccess();

    private static class Success<T> extends Result<T> {
        final T t;

        Success(final T t) {
            this.t = t;
        }

        @Override
        public Optional<Exception> executeOrGetError(final Consumer<T> consumer) {
            consumer.accept(t);
            return Optional.empty();
        }

        @Override
        public void executeOrThrow(final Consumer<T> consumer) {
            consumer.accept(t);
        }

        @Override
        public <U> Result<U> map(final Function<T, U> f) {
            try {
                return success(f.apply(t));
            } catch (Exception e) {
                return failure(e);
            }
        }

        @Override
        public <U> Result<U> flatMap(final Function<T, Result<U>> f) {
            try {
                return f.apply(t);
            } catch (Exception e) {
                return failure(e);
            }
        }

        @Override
        public void bind(final Consumer<T> success, final Consumer<Exception> failure) {
            success.accept(t);
        }

        @Override
        public Result<T> mapFailure(final String s) {
            return this;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    private static class Failure<T> extends Result<T> {
        private final Exception exception;

        Failure(final String message) {
            this.exception = new IllegalArgumentException(message);
        }

        Failure(final Exception exception) {
            this.exception = exception;
        }

        @Override
        public Optional<Exception> executeOrGetError(final Consumer<T> consumer) {
            return Optional.of(exception);
        }

        @Override
        public void executeOrThrow(final Consumer<T> consumer) {
            throw new RuntimeException(exception);
        }

        @Override
        public <U> Result<U> map(final Function<T, U> f) {
            return failure(exception);
        }

        @Override
        public <U> Result<U> flatMap(final Function<T, Result<U>> f) {
            return failure(exception);
        }

        @Override
        public void bind(final Consumer<T> success, final Consumer<Exception> failure) {
            failure.accept(exception);
        }

        @Override
        public Result<T> mapFailure(final String s) {
            return failure(s);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }
    }
}
