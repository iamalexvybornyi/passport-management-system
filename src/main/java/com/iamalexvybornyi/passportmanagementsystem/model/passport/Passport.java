package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.persistence.*;
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
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate givenDate;

    @NonNull
    private String departmentCode;

    @NonNull
    private PassportType passportType;

    @NonNull
    private Status status;
    
    public Passport() {
        this.status = Status.ACTIVE;
    }

}
