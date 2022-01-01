package project.model.exceptions;

import project.model.util.UtilMethods;

/**
 * A custom exception for better output and specific exception handling.
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 */
public class NamedException extends Exception {
    private final String FULL_MESSAGE;
    private final String SIMPLE_MESSAGE;

    public NamedException(String _msg) {
        FULL_MESSAGE = _msg;
        SIMPLE_MESSAGE = FULL_MESSAGE;
    }

    public NamedException(Throwable throwable) {
        SIMPLE_MESSAGE = UtilMethods.getRootCause(throwable).toString();
        FULL_MESSAGE = throwable + " (" + SIMPLE_MESSAGE + ")";
    }

    public String getSimpleMessage() {
        return SIMPLE_MESSAGE;
    }

    public String getFullMessage() {
        return FULL_MESSAGE;
    }
}
