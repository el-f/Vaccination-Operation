package project.model.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * A class for utility methods.
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 */
public class UtilMethods {

    /**
     * Get a timestamp of the exact current {@link LocalDateTime}.
     * @return a timestamp instance.
     */
    public static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * Get a timestamp of a {@link LocalDateTime} {@code n} days from now.
     * @param nDays the amount of days the timestamp is from now.
     * @return a timestamp instance.
     */
    public static Timestamp nDaysFromNow(int nDays) {
        return Timestamp.valueOf(LocalDateTime.now().plusDays(nDays));
    }

    /**
     * Get the root cause of a throwable by traversing its cause stack.
     * @param throwable the throwable we investigate.
     * @return the root throwable cause for the input throwable.
     */
    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        Throwable result = throwable;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

}
