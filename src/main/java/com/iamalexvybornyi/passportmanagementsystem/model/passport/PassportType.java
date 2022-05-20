package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PassportType {
    INTERNAL,
    INTERNATIONAL;

    @JsonCreator
    public static PassportType getPassportTypeFromString(String value) {
        for (PassportType passportType : PassportType.values()) {
            if (passportType.toString().equalsIgnoreCase(value)) {
                return passportType;
            }
        }
        return null;
    }
}
