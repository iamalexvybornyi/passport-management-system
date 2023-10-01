package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
@RequiredArgsConstructor
public class PersonRepositoryCollectionImpl implements PersonRepository {

    @NonNull
    private final Set<Person> persons = new HashSet<>();

    @NonNull
    private final PassportRepository passportRepository;

    @Override
    @NonNull
    public Person save(@NonNull Person person) {
        persons.add(person);
        return person;
    }

    @Override
    @Nullable
    public Person findById(@NonNull String id) {
        return this.persons.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    @NonNull
    public Iterable<Person> findAll() {
        return this.persons;
    }

    @Override
    @Nullable
    public Person findByPassportNumber(@NonNull String passportNumber) {
        final Passport passport = passportRepository.findByPassportNumber(passportNumber);
        if (passport == null) {
            return null;
        }
        return persons
                .stream().filter(person -> person.getId().equals(passport.getPersonId()))
                .findFirst().orElse(null);
    }

    @Override
    public void delete(@NonNull Person person) {
        this.persons.remove(person);
    }

    @Override
    public void deleteAll() {
        this.persons.clear();
    }

}
