package interfaces;

public interface Worker {
    void start();
    void startAfter(final Worker... workers);
    void stop();
    void monitor(final Monitor monitor);
    String name();
}
