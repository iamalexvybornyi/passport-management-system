package com.iamalexvybornyi.passportmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class Person {

    @Id
    private @NonNull String id;

    @NonNull
    private String name;

    @NonNull
    private List<Passport> passports;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate birthDate;

    @NonNull
    private String birthCountry;
}
