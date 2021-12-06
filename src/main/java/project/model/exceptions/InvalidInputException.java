package project.model.exceptions;

/**
 * Thrown for any invalid input given by the user.
 * Extending {@link NamedException} and so has better cause handling than a regular exception.
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 */
public class InvalidInputException extends NamedException {
    public InvalidInputException(String _msg) {
        super(_msg);
    }

    public InvalidInputException(Throwable throwable) {
        super(throwable);
    }
}
