package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

@EqualsAndHashCode
@Data
public class Passport {

    @NonNull
    @Id
    private String id;

    @NonNull
    private String passportNumber;

    @NonNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate givenDate;

    @NonNull
    private String departmentCode;

    @NonNull
    private PassportType passportType;

    @NonNull
    private Status status;
    @NonNull
    private String personId;
    
    public Passport() {
        this.status = Status.ACTIVE;
    }

}
