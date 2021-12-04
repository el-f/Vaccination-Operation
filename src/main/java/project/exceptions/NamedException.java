package project.exceptions;

public abstract class NamedException extends Exception {
    private final String FULL_MESSAGE;

    public NamedException(String _msg) {
        FULL_MESSAGE = _msg;
    }

    public String toString() {
        return FULL_MESSAGE;
    }
}
