package com.krm.service;


import com.krm.model.Person;

import java.util.UUID;

public interface PersonService {

    Person save(Person person);

    Person update(Person person);

    Person findOne(UUID uuid);

    void delete(UUID uuid);

}
