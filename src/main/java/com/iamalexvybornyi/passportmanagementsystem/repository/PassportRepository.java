package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface PassportRepository {

    @NonNull
    Passport save(@NonNull Passport passport);

    @Nullable
    Passport findById(@NonNull String id);

    @Nullable
    Passport findByPassportNumber(@NonNull String passportNumber);

    @NonNull
    List<Passport> findByPersonId(@NonNull String personId);

    @NonNull
    Iterable<Passport> findAll();

    @NonNull
    Iterable<Passport> findByGivenDateBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate);

    void delete(@NonNull Passport passport);

    void deleteAll();

}
