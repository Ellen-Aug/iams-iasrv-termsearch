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
}
