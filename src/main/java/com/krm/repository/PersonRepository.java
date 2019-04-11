package com.krm.repository;


import com.krm.model.Person;

import java.util.UUID;

public interface PersonRepository {

    Person save(Person person);

    Person update(Person person);

    Person findOne(UUID uuid);

    void delete(UUID uuid);

}
