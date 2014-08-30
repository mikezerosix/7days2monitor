package org.mikezerosix;

import com.jolbox.bonecp.BoneCPDataSource;
import org.mikezerosix.entities.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
import java.sql.SQLException;
import java.sql.Statement;

@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement
@Configuration
public class JdbcConfiguration {
    private final Logger log = LoggerFactory.getLogger(JdbcConfiguration.class);

    @Bean
    public DataSource dataSource() {
        String driverStr = "org.apache.derby.jdbc.EmbeddedDriver";
        String jdbc = "jdbc:derby:7days2monitor;create=true";
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
        factory.setPackagesToScan(Settings.class.getPackage().getName());
        factory.setDataSource(dataSource());

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
/*

    void initDB() throws SQLException {
        Connection connection = connectionProvider().get();
        createTable(connection, "CREATE TABLE settings (id varchar(64) not null PRIMARY KEY, value varchar(256))");
        createTable(connection, "CREATE TABLE server (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), host varchar(64), game_port int, telnet_port int, ftp_port int, telnet_password varchar(64))");
        createTable(connection, "CREATE TABLE chat (playerId bigint not null PRIMARY KEY, name varchar(40), ts timestamp default  CURRENT_TIMESTAMP not null, msg  LONG VARCHAR)");


    }

    private void createTable(Connection connection, String ddl) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute(ddl);
            System.out.println("Created table: " + ddl.split(" ")[2]);
        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")){
                throw e;
            }
        }
    }
*/

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }
}
