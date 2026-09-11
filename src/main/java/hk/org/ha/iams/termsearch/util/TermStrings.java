package hk.org.ha.iams.termsearch.util;

import java.util.Collection;
import java.util.Iterator;

public final class TermStrings {

    private TermStrings() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String join(Collection<?> values, String separator) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        String sep = separator == null ? "" : separator;
        StringBuilder builder = new StringBuilder();
        Iterator<?> it = values.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next != null) {
                builder.append(next);
            }
            if (it.hasNext()) {
                builder.append(sep);
            }
        }
        return builder.toString();
    }
}
