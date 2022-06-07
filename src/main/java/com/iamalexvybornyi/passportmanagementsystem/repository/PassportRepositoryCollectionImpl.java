package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PassportRepositoryCollectionImpl implements PassportRepository {

    private final Set<Passport> passports = new HashSet<>();

    private final PersonRepository personRepository;

    @Autowired
    public PassportRepositoryCollectionImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Passport save(Passport passport) {
        this.passports.add(passport);
        return passport;
    }

    @Override
    public Passport findById(Long id) {
        return this.passports.stream()
                .filter(passport -> passport.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Passport findByPassportNumber(String passportNumber) {
        return this.passports.stream()
                .filter(passport -> passport.getPassportNumber().equals(passportNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Iterable<Passport> findAll() {
        return this.passports;
    }

    @Override
    public Iterable<Passport> findByGivenDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Passport> foundPassports = new ArrayList<>(this.passports);
        if (startDate != null) {
            foundPassports = foundPassports
                    .stream()
                    .filter(passport -> passport.getGivenDate().compareTo(startDate) >= 0)
                    .collect(Collectors.toList());
        }
        if (endDate != null) {
            foundPassports = foundPassports
                    .stream()
                    .filter(passport -> passport.getGivenDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }
        return foundPassports;
    }

    @Override
    public void delete(Passport passport) {
        Person person = personRepository.findByPassportNumber(passport.getPassportNumber());
        personRepository.findById(person.getId()).getPassports().remove(passport);
        this.passports.remove(passport);
    }

    @Override
    public void deleteAll() {
        Iterator<Passport> passportIterator = this.passports.iterator();
        while (passportIterator.hasNext()) {
            Passport passport = passportIterator.next();
            Person person = personRepository.findByPassportNumber(passport.getPassportNumber());
            if (person != null) {
                personRepository.findById(person.getId()).getPassports().remove(passport);
            }
            passportIterator.remove();
        }
    }

}
