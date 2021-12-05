package project.model.exceptions;

public class DatabaseQueryException extends NamedException {
    public DatabaseQueryException(String _msg) {
        super(_msg);
    }

    public DatabaseQueryException(Throwable throwable) {
        super(throwable);
    }
}
