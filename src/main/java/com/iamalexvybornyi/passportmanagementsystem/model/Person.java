package com.iamalexvybornyi.passportmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@EqualsAndHashCode
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
}
