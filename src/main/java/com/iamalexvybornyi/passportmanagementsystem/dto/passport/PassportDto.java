package com.iamalexvybornyi.passportmanagementsystem.dto.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.validation.UniquePassportNumber;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class PassportDto {

    @NotNull
    private Long id;

    @NotNull
//    @UniquePassportNumber
    @Size(min = 5, max = 20)
    private String passportNumber;

    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate givenDate;

    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}", message = "Invalid department code format!")
    private String departmentCode;

    @NotNull
    private PassportType passportType;

    @NotNull
    private Status status;

}
