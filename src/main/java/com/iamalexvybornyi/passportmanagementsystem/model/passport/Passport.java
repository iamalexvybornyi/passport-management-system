package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@EqualsAndHashCode
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

    private static AtomicLong idCounter = new AtomicLong();

    public Passport() {
        this.id = idCounter.incrementAndGet();
        this.status = Status.ACTIVE;
    }

}
