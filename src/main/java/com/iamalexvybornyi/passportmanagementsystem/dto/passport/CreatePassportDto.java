package com.iamalexvybornyi.passportmanagementsystem.dto.passport;

import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class CreatePassportDto {

    @NotNull
    @Size(min = 5, max = 20)
    private String passportNumber;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "{passport.management.system.constraints.given.date.message}")
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
}
