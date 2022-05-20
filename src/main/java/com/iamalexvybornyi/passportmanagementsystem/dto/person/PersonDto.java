package com.iamalexvybornyi.passportmanagementsystem.dto.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iamalexvybornyi.passportmanagementsystem.validation.ValidBirthDate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class PersonDto {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 5, max = 75)
    private String name;

    @NotNull
//    @ValidBirthDate
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate birthDate;

    @NotNull
    @Size(min = 2, max = 50)
    private String birthCountry;

}
