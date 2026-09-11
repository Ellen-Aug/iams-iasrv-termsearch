package hk.org.ha.iams.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

/**
 * Local UCP bean. No EHR common-ucp. Helm ucp.enabled is the platform toggle; this bean owns the Java pool.
 */
@Configuration
@ConditionalOnProperty(name = "termsearch.datasource.enabled", havingValue = "true")
public class UcpDataSourceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(UcpDataSourceConfig.class);

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password:}") String password,
            @Value("${spring.datasource.oracleucp.connection-pool-name:IamsTermsearchUCP}") String poolName,
            @Value("${spring.datasource.oracleucp.fast-connection-failover-enabled:true}") boolean fcf,
            @Value("${spring.datasource.oracleucp.initial-pool-size:1}") int initialSize,
            @Value("${spring.datasource.oracleucp.min-pool-size:1}") int minSize,
            @Value("${spring.datasource.oracleucp.max-pool-size:10}") int maxSize) throws SQLException {

        PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
        pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        pds.setURL(url);
        pds.setUser(username);
        if (StringUtils.hasText(password)) {
            pds.setPassword(password);
        }
        pds.setConnectionPoolName(poolName);
        pds.setInitialPoolSize(initialSize);
        pds.setMinPoolSize(minSize);
        pds.setMaxPoolSize(maxSize);
        pds.setValidateConnectionOnBorrow(true);
        pds.setSQLForValidateConnection("SELECT 1 FROM DUAL");
        pds.setFastConnectionFailoverEnabled(fcf);
        Properties props = new Properties();
        props.setProperty("oracle.net.CONNECT_TIMEOUT", "120000");
        pds.setConnectionProperties(props);
        LOG.info("UCP pool {} created (FCF={})", poolName, fcf);
        return pds;
    }
}
