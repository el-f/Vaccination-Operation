package project.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Utils {

    static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

}
