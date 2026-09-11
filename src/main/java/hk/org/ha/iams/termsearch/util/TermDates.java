package hk.org.ha.iams.termsearch.util;

import java.sql.Timestamp;
import java.util.Date;

public final class TermDates {

    private TermDates() {
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date toDate(Timestamp timestamp) {
        return timestamp == null ? null : new Date(timestamp.getTime());
    }

    /**
     * Oracle JDBC + {@code SqlRowSet}/{@code CachedRowSet} cannot cast
     * {@code oracle.sql.TIMESTAMP} to {@code java.sql.Timestamp}.
     */
    public static Date fromJdbc(Object raw) {
        if (raw == null) {
            return null;
        }
        if (raw instanceof Timestamp timestamp) {
            return new Date(timestamp.getTime());
        }
        if (raw instanceof java.sql.Date sqlDate) {
            return new Date(sqlDate.getTime());
        }
        if (raw instanceof Date date) {
            return date;
        }
        try {
            Object timestamp = raw.getClass().getMethod("timestampValue").invoke(raw);
            if (timestamp instanceof Timestamp ts) {
                return new Date(ts.getTime());
            }
            if (timestamp instanceof Date date) {
                return date;
            }
        } catch (ReflectiveOperationException ignored) {
            // not an Oracle TIMESTAMP
        }
        return null;
    }
}
