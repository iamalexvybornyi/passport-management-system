package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//@Repository
@RequiredArgsConstructor
public class PassportRepositoryCollectionImpl implements PassportRepository {

    @NonNull
    private final Set<Passport> passports = new HashSet<>();

    @Override
    @NonNull
    public Passport save(@NonNull Passport passport) {
        this.passports.add(passport);
        return passport;
    }

    @Override
    @Nullable
    public Passport findById(@NonNull String id) {
        return this.passports.stream()
                .filter(passport -> passport.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    @Nullable
    public Passport findByPassportNumber(@NonNull String passportNumber) {
        return this.passports.stream()
                .filter(passport -> passport.getPassportNumber().equals(passportNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    @NonNull
    public List<Passport> findByPersonId(@NonNull String personId) {
        return this.passports.stream()
                .filter(passport -> passport.getPersonId().equals(personId))
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public Iterable<Passport> findAll() {
        return this.passports;
    }

    @Override
    @NonNull
    public Iterable<Passport> findByGivenDateBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
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
    public void delete(@NonNull Passport passport) {
        this.passports.remove(passport);
    }

    @Override
    public void deleteAll() {
        final Iterator<Passport> passportIterator = this.passports.iterator();
        while (passportIterator.hasNext()) {
            passportIterator.next();
            passportIterator.remove();
        }
    }

}
