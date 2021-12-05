package project.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Utils {

    public static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static Timestamp nDaysFromNow(int nDays) {
        return Timestamp.valueOf(LocalDateTime.now().plusDays(nDays));
    }

    public static Throwable getRootCause(Throwable e) {
        Throwable cause;
        Throwable result = e;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

}
