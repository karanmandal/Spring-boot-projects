package com.krm.repository;

import com.krm.model.Person;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CassandraPersonRepository implements PersonRepository {

    private final CassandraOperations cassandraTemplate;

    public CassandraPersonRepository(CassandraOperations cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

    @Override
    public Person save(Person person) {
        return this.cassandraTemplate.insert(person);
    }

    @Override
    public Person update(Person person) {
        return this.cassandraTemplate.update(person);
    }

    @Override
    public Person findOne(UUID uuid) {
        return this.cassandraTemplate.selectOneById(uuid, Person.class);
    }

    @Override
    public void delete(UUID uuid) {
        this.cassandraTemplate.deleteById(uuid, Person.class);
    }

}
