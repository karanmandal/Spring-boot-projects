package com.krm.config;

import com.krm.repository.PersonRepository;
import com.krm.service.PersonService;
import com.krm.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource(value = {"classpath:application.yml"})
@ConfigurationProperties("spring.data.cassandra")
@EnableCassandraRepositories
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Bean
    public PersonService personService(PersonRepository personRepository) {
        return new PersonServiceImpl(personRepository);
    }

    @Value("${keyspacename}")
    private String keySpacename;

    @Value("${basepackage}")
    private String basePackage;

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Arrays.asList(CreateKeyspaceSpecification.createKeyspace(keySpacename).ifNotExists()
                .withSimpleReplication());
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getKeyspaceName() {
        return keySpacename;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{basePackage};
    }

    @Override
    protected boolean getMetricsEnabled() {
        return false;
    }
}

