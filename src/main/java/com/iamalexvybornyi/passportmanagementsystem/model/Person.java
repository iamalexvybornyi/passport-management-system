package com.iamalexvybornyi.passportmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private List<Passport> passports;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate birthDate;

    private String birthCountry;

    private static AtomicLong idCounter = new AtomicLong();

    public Person() {
        this.id = idCounter.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id) &&
                name.equals(person.name) &&
                birthDate.equals(person.birthDate) &&
                birthCountry.equals(person.birthCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
