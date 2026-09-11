package hk.org.ha.iams.termsearch.dao;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import hk.org.ha.iams.termsearch.dao.vo.LookupValueDTO;

/**
 * JDBC stand-in for hk.org.ha.iams.common.cache.LookupValueCache.
 * Tables: iams_lookup_value / iams_lookup_list (HA IAMS common entities).
 */
@Component
public class LookupValueCache {

    private static final Logger LOG = LoggerFactory.getLogger(LookupValueCache.class);

    private static final String BY_KEY_SQL =
            "SELECT data_value FROM iams_lookup_value WHERE value_key = :valueKey";

    private static final String BY_LIST_SQL =
            "SELECT v.value_key, v.data_value "
                    + "FROM iams_lookup_value v "
                    + "INNER JOIN iams_lookup_list l ON l.list_key = v.list_key "
                    + "WHERE l.list_name = :listName AND UPPER(TRIM(v.data_value)) = UPPER(TRIM(:dataValue))";

    private final NamedParameterJdbcTemplate jdbc;

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
            LOG.warn("lookup by value_key {} failed: {}", valueKey, ex.getMessage());
            return new LookupValueDTO(valueKey, String.valueOf(valueKey));
        }
    }

    public LookupValueDTO getValueByDataValue(String listName, String dataValue) {
        if (listName == null || dataValue == null || jdbc == null) {
            return null;
        }
        try {
            return jdbc.query(BY_LIST_SQL, Map.of("listName", listName, "dataValue", dataValue), rs -> {
                if (rs.next()) {
                    return new LookupValueDTO(rs.getInt("value_key"), rs.getString("data_value"));
                }
                return null;
            });
        } catch (RuntimeException ex) {
            LOG.warn("lookup list {} value {} failed: {}", listName, dataValue, ex.getMessage());
            return null;
        }
    }
}
