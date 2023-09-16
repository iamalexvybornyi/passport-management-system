package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NonNull;

import java.util.Arrays;

public enum PassportType {
    INTERNAL,
    INTERNATIONAL;

    @NonNull
    @JsonCreator
    public static PassportType getPassportTypeFromString(@NonNull String value) {
        for (PassportType passportType : PassportType.values()) {
            if (passportType.toString().equalsIgnoreCase(value)) {
                return passportType;
            }
        }
        throw new IllegalArgumentException(
                String.format("'%s' is not a valid Passport Type, must be one of the following values: %s",
                        value, Arrays.toString(PassportType.values())));
    }
}
