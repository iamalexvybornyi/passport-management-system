package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Status {
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static Status getPassportStatusFromString(String value) {
        for (Status status : Status.values()) {
            if (status.toString().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(
                String.format("'%s' is not a valid Passport Status, must be one of the following values: %s",
                        value, Arrays.toString(Status.values())));
    }
}
