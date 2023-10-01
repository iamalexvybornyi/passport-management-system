package com.iamalexvybornyi.passportmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class Person {

    @Id
    @NonNull
    private String id;

    @NonNull
    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    @NonNull
    private String birthCountry;
}
