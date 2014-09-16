package org.mikezerosix;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mikezerosix.entities.StatRepository;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement
@Import(JdbcConfiguration.class)
public class AppTest  {


}
