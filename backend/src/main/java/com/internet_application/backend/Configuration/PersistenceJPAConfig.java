package com.internet_application.backend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * See https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa for more information on how this class works
 */
@Configuration
@EnableTransactionManagement
public class PersistenceJPAConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.internet_application.backend.Entities" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/aipolito");
        // mvn -Dmaven.test.skip=true packagedataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        // dataSource.setUrl("jdbc:postgresql://database:5432/postgres");
        dataSource.setUsername( "postgres" );
        dataSource.setPassword( "aipolito" );
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        // properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // create / create-drop / update
        properties.setProperty("hibernate.show_sql", "true");
        // Set the initialization of the database to happen at every startup
        properties.setProperty("spring.datasource.initialization-mode", "always");
        // Schema path that needs to be initialized
        properties.setProperty("javax.persistence.schema-generation.database.action", "drop-and-create");
        properties.setProperty("javax.persistence.schema-generation.create-source", "script");
        properties.setProperty("javax.persistence.schema-generation.create-script-source", "META-INF/schema.sql");
        //properties.setProperty("spring.datasource.schema", "META-INF/schema.sql");
        // Continue application startup in spite of any errors in data initialization
        properties.setProperty("spring.datasource.continue-on-error", "true");

        // Guys at JDBC were lazy and didn't implement createClob, so if you don't put this line everything explodes
        properties.setProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", "true");

        // Enable logging of sql statements
        properties.setProperty("logging.level.org.hibernate.SQL", "DEBUG");
        properties.setProperty("logging.level.org.hibernate.type.descriptor.sql.BasicBinder", "TRACE");

        //properties.setProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", "false");
        //properties.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQL95Dialect");
        //properties.setProperty("hibernate.format_sql", "true");

        return properties;
    }
}
