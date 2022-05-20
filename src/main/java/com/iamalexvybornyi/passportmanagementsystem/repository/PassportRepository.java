package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;

import java.time.LocalDate;

public interface PassportRepository {

    Passport save(Passport passport);

    Passport findById(Long id);

    Passport findByPassportNumber(String passportNumber);

    Iterable<Passport> findAll();

    Iterable<Passport> findByGivenDateBetween(LocalDate startDate, LocalDate endDate);

    void delete(Passport passport);

}
