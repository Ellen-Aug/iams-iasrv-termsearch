package hk.org.ha.iams.termsearch.util;

import java.util.Collection;
import java.util.Map;

public final class TermCollections {

    private TermCollections() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
