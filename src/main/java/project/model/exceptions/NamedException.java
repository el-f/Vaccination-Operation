package project.model.exceptions;

import project.model.Utils;

public abstract class NamedException extends Exception {
    private final String FULL_MESSAGE;

    public NamedException(String _msg) {
        FULL_MESSAGE = _msg;
    }

    public NamedException(Throwable throwable) {
        FULL_MESSAGE = throwable + " (" + Utils.getRootCause(throwable) + ")";
    }

    public String getFullMessage() {
        return FULL_MESSAGE;
    }
}
