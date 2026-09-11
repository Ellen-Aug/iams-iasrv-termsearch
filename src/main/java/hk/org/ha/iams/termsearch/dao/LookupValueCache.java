package hk.org.ha.iams.termsearch.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import hk.org.ha.iams.termsearch.dao.vo.LookupValueDTO;

/**
 * JDBC stand-in for hk.org.ha.iams.common.cache.LookupValueCache.
 * IAMS_VALUE has no LIST_KEY — membership is IAMS_VALUE_SET
 * (LIST_KEY + VALUE_KEY) joined to IAMS_VALUE_LIST.LIST_NAME.
 */
@Component
public class LookupValueCache {

    private static final Logger LOG = LoggerFactory.getLogger(LookupValueCache.class);

    private static final String BY_KEY_SQL =
            "SELECT data_value FROM IAMS_VALUE WHERE value_key = :valueKey";

    private static final String BY_LIST_SQL =
            "SELECT v.value_key, v.data_value "
                    + "FROM IAMS_VALUE v "
                    + "INNER JOIN IAMS_VALUE_SET s ON s.value_key = v.value_key "
                    + "INNER JOIN IAMS_VALUE_LIST l ON l.list_key = s.list_key "
                    + "WHERE l.list_name = :listName "
                    + "AND UPPER(TRIM(v.data_value)) = UPPER(TRIM(:dataValue))";

    private final NamedParameterJdbcTemplate jdbc;
    private final ConcurrentHashMap<Integer, LookupValueDTO> byKey = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LookupValueDTO> byListValue = new ConcurrentHashMap<>();

    public LookupValueCache(ObjectProvider<DataSource> dataSources) {
        DataSource dataSource = dataSources.getIfAvailable();
        this.jdbc = dataSource == null ? null : new NamedParameterJdbcTemplate(dataSource);
    }

    public LookupValueDTO getValueByValueKey(int valueKey) {
        return getValueByValueKey(Integer.valueOf(valueKey));
    }

    public LookupValueDTO getValueByValueKey(Integer valueKey) {
        if (valueKey == null) {
            return new LookupValueDTO(null, null);
        }
        LookupValueDTO cached = byKey.get(valueKey);
        if (cached != null) {
            return cached;
        }
        LookupValueDTO resolved = lookupByKey(valueKey);
        byKey.put(valueKey, resolved);
        return resolved;
    }

    public LookupValueDTO getValueByDataValue(String listName, String dataValue) {
        if (listName == null || dataValue == null || jdbc == null) {
            return null;
        }
        String cacheKey = listName + '\0' + dataValue.toUpperCase();
        if (byListValue.containsKey(cacheKey)) {
            return byListValue.get(cacheKey);
        }
        LookupValueDTO resolved = lookupByList(listName, dataValue);
        if (resolved != null) {
            byListValue.put(cacheKey, resolved);
            if (resolved.getValueKey() != null) {
                byKey.putIfAbsent(resolved.getValueKey(), resolved);
            }
        }
        return resolved;
    }

    private LookupValueDTO lookupByKey(Integer valueKey) {
        if (jdbc == null) {
            return new LookupValueDTO(valueKey, String.valueOf(valueKey));
        }
        try {
            String dataValue = jdbc.query(BY_KEY_SQL, Map.of("valueKey", valueKey), rs -> {
                if (rs.next()) {
                    return rs.getString("data_value");
                }
                return null;
            });
            return new LookupValueDTO(valueKey, dataValue != null ? dataValue : String.valueOf(valueKey));
        } catch (RuntimeException ex) {
            LOG.warn("lookup by value_key {} failed: {}", valueKey, rootMessage(ex));
            return new LookupValueDTO(valueKey, String.valueOf(valueKey));
        }
    }

    private LookupValueDTO lookupByList(String listName, String dataValue) {
        try {
            return jdbc.query(BY_LIST_SQL, Map.of("listName", listName, "dataValue", dataValue), rs -> {
                if (rs.next()) {
                    return new LookupValueDTO(rs.getInt("value_key"), rs.getString("data_value"));
                }
                return null;
            });
        } catch (RuntimeException ex) {
            LOG.warn("lookup list {} value {} failed: {}", listName, dataValue, rootMessage(ex));
            return null;
        }
    }

    private static String rootMessage(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage() != null ? root.getMessage() : ex.getMessage();
    }
}
