package com.iamalexvybornyi.passportmanagementsystem.dto.passport;

import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.validation.ValueOfEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Data
public class PassportWithPersonDto {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 5, max = 20)
    private String passportNumber;

    @NotNull
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "{passport.management.system.constraints.given.date.message}")
    private String givenDate;

    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}", message = "{passport.management.system.constraints.department.code.message}")
    private String departmentCode;

    @NotNull
    @ValueOfEnum(enumClass = PassportType.class, message = "{passport.management.system.constraints.passport.type.message}")
    private String passportType;

    @NotNull
    @ValueOfEnum(enumClass = Status.class, message = "{passport.management.system.constraints.passport.status.message}")
    private String status;

    private PersonDto person;
}
