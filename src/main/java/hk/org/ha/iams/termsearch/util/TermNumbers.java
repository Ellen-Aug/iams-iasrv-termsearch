package hk.org.ha.iams.termsearch.util;

import java.text.DecimalFormat;

public final class TermNumbers {

    private TermNumbers() {
    }

    public static Integer toInteger(String value) {
        if (TermStrings.isBlank(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            try {
                return (int) Double.parseDouble(value.trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
    }

    public static Double toDouble(String value) {
        if (TermStrings.isBlank(value)) {
            return null;
        }
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static String formatNumber(Double value, String pattern) {
        if (value == null) {
            return null;
        }
        return new DecimalFormat(pattern == null ? "0.00" : pattern).format(value);
    }
}
