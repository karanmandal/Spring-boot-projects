package com.krm.service;


import com.krm.model.Person;
import com.krm.repository.PersonRepository;

import java.util.UUID;

public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) {
        if (person.getId() == null) {
            person.setId(UUID.randomUUID());
        }
        this.personRepository.save(person);
        return person;
    }

    @Override
    public Person update(Person person) {
        Person existingPerson = this.personRepository.findOne(person.getId());
        if (existingPerson != null) {
            this.personRepository.update(person);
        }
        return person;
    }

    @Override
    public Person findOne(UUID uuid) {
        return this.personRepository.findOne(uuid);
    }

    @Override
    public void delete(UUID uuid) {
        Person person = this.personRepository.findOne(uuid);
        if (person != null) {
            this.personRepository.delete(uuid);
        }
    }

}
