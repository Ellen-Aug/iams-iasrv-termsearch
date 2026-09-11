package hk.org.ha.iams.termsearch.dao;

import java.util.List;
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
 * JDBC stand-in for hk.org.ha.iams.common.cache.LookupValueCache
 * ({@code LookupValueEntity} / {@code LookupValueListEntity}).
 */
@Component
public class LookupValueCache {

    private static final Logger LOG = LoggerFactory.getLogger(LookupValueCache.class);

    private static final List<String> VALUE_TABLES = List.of("LOOKUP_VALUE", "iams_lookup_value");
    private static final List<String> LIST_TABLES = List.of("LOOKUP_LIST", "iams_lookup_list");

    private final NamedParameterJdbcTemplate jdbc;
    private final ConcurrentHashMap<Integer, LookupValueDTO> byKey = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LookupValueDTO> byListValue = new ConcurrentHashMap<>();

    private volatile String valueTable;
    private volatile String listTable;
    private volatile boolean tablesResolved;

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
        resolveTables();
        if (valueTable == null) {
            return new LookupValueDTO(valueKey, String.valueOf(valueKey));
        }
        try {
            String sql = "SELECT data_value FROM " + valueTable + " WHERE value_key = :valueKey";
            String dataValue = jdbc.query(sql, Map.of("valueKey", valueKey), rs -> {
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
        resolveTables();
        if (valueTable == null || listTable == null) {
            return null;
        }
        try {
            String sql = "SELECT v.value_key, v.data_value FROM " + valueTable + " v "
                    + "INNER JOIN " + listTable + " l ON l.list_key = v.list_key "
                    + "WHERE l.list_name = :listName AND UPPER(TRIM(v.data_value)) = UPPER(TRIM(:dataValue))";
            return jdbc.query(sql, Map.of("listName", listName, "dataValue", dataValue), rs -> {
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

    private void resolveTables() {
        if (tablesResolved || jdbc == null) {
            return;
        }
        synchronized (this) {
            if (tablesResolved) {
                return;
            }
            valueTable = firstExisting(VALUE_TABLES);
            listTable = firstExisting(LIST_TABLES);
            tablesResolved = true;
            LOG.info("lookup tables value={} list={}", valueTable, listTable);
        }
    }

    private String firstExisting(List<String> tables) {
        for (String table : tables) {
            try {
                jdbc.getJdbcOperations().execute("SELECT 1 FROM " + table + " WHERE 1 = 0");
                return table;
            } catch (RuntimeException ex) {
                LOG.info("lookup table {} skipped: {}", table, rootMessage(ex));
            }
        }
        return null;
    }

    private static String rootMessage(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage() != null ? root.getMessage() : ex.getMessage();
    }
}
