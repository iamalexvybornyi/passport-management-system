package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.Person;

public interface PersonRepository {

    Person save(Person person);

    Person findById(Long id);

    Iterable<Person> findAll();

    Person findByPassportNumber(String passportNumber);

    void delete(Person person);

    void deleteAll();
}
