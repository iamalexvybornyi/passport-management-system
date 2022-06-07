package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class PersonRepositoryCollectionImpl implements PersonRepository {

    Set<Person> persons = new HashSet<>();

    @Override
    public Person save(Person person) {
        persons.add(person);
        return person;
    }

    @Override
    public Person findById(Long id) {
        return this.persons.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Iterable<Person> findAll() {
        return this.persons;
    }

    @Override
    public Person findByPassportNumber(String passportNumber) {
        return persons
                .stream().filter(person -> {
                    List<Passport> passports = person.getPassports();
                    if (passports != null) {
                        return person.getPassports()
                                .stream().anyMatch(passport -> passport.getPassportNumber().equals(passportNumber));
                    } else {
                        return false;
                    }
                })
                .findFirst().orElse(null);
    }

    @Override
    public void delete(Person person) {
        this.persons.remove(person);
    }

    @Override
    public void deleteAll() {
        this.persons.clear();
    }

}
