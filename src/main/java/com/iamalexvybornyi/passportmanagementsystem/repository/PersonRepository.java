package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import lombok.NonNull;
import org.springframework.lang.Nullable;

public interface PersonRepository {

    @NonNull
    Person save(@NonNull Person person);

    @Nullable
    Person findById(@NonNull String id);

    @NonNull
    Iterable<Person> findAll();

    @Nullable
    Person findByPassportNumber(@NonNull String passportNumber);

    void delete(@NonNull Person person);

    void deleteAll();
}
