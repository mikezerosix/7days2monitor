package org.mikezerosix;

import com.jolbox.bonecp.BoneCPDataSource;
import org.mikezerosix.entities.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement
@Configuration
public class JdbcConfiguration {
    private final Logger log = LoggerFactory.getLogger(JdbcConfiguration.class);
    private static Properties properties = new Properties();
    static {
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
    }
    @Bean
    public DataSource dataSource() {
        String driverStr = "org.apache.derby.jdbc.EmbeddedDriver";
        String jdbc = "jdbc:derby:db;create=true";
        String username = "";
        String password = "";
        log.info("DB driver={}, url={}", driverStr, jdbc);
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(driverStr);
        dataSource.setJdbcUrl(jdbc);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Sets up a {@link LocalContainerEntityManagerFactoryBean} to use Hibernate.
     * Activates picking up entities from the project's base package.
     *
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.DERBY);
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(Setting.class.getPackage().getName());
        factory.setDataSource(dataSource());
        factory.setJpaProperties(properties);
        return factory;
    }

    @Bean
    public Provider<Connection> connectionProvider() {
        return new Provider<Connection>() {
            final DataSource dataSource = dataSource();

            @Override
            public Connection get() {
                Connection conn = DataSourceUtils.getConnection(dataSource);

                return conn;
            }

        };
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }
}
