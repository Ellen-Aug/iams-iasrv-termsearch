package hk.org.ha.iams.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@ConditionalOnBean(DataSource.class)
@EnableTransactionManagement
public class EclipseLinkJpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPersistenceUnitName("iamsPersistenceUnit");
        emf.setPackagesToScan("hk.org.ha.iams.termsearch.jpa.entity");
        emf.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
        Map<String, Object> props = new HashMap<>();
        props.put("eclipselink.weaving", "false");
        props.put("eclipselink.cache.shared.default", "false");
        props.put("eclipselink.logging.level", "INFO");
        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
