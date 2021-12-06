package project.model.exceptions;

/**
 * Thrown when any query to the database has failed for any reason.
 * Extending {@link NamedException} and so has better cause handling than a regular exception.
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 */
public class DatabaseQueryException extends NamedException {
    public DatabaseQueryException(String _msg) {
        super(_msg);
    }

    public DatabaseQueryException(Throwable throwable) {
        super(throwable);
    }
}
