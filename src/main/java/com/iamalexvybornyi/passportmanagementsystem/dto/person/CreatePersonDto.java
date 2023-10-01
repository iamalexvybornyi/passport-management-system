package com.iamalexvybornyi.passportmanagementsystem.dto.person;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class CreatePersonDto {

    @NotNull
    @Size(min = 5, max = 75)
    private String name;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "{passport.management.system.constraints.birth.date.format.message}")
    private String birthDate;

    @NotNull
    @Size(min = 2, max = 50)
    private String birthCountry;

}
