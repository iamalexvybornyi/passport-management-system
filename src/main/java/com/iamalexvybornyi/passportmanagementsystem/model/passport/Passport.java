package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Passport {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate givenDate;

    private String departmentCode;

    private PassportType passportType;

    private Status status;

//    private Person person;

    private static AtomicLong idCounter = new AtomicLong();

    public Passport() {
        this.id = idCounter.incrementAndGet();
        this.status = Status.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passport passport = (Passport) o;
        return id.equals(passport.id) &&
                passportNumber.equals(passport.passportNumber) &&
                givenDate.equals(passport.givenDate) &&
                departmentCode.equals(passport.departmentCode) &&
                passportType == passport.passportType &&
                status.equals(passport.status);
//                person.equals(passport.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
