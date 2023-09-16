package com.iamalexvybornyi.passportmanagementsystem.dto.person;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Data
public class PersonDto {

    @NotNull
    private String id;

    @NotNull
    @Size(min = 5, max = 75)
    private String name;

    @NotNull
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "{passport.management.system.constraints.birth.date.format.message}")
    private String birthDate;

    @NotNull
    @Size(min = 2, max = 50)
    private String birthCountry;
}
