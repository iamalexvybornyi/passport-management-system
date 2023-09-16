package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NonNull;

import java.util.Arrays;

public enum Status {
    ACTIVE,
    INACTIVE;

    @NonNull
    @JsonCreator
    public static Status getPassportStatusFromString(@NonNull String value) {
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
